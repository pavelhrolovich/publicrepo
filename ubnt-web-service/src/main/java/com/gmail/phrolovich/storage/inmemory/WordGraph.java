package com.gmail.phrolovich.storage.inmemory;

import com.gmail.phrolovich.storage.SubredditStatistic;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

class WordGraph {
    private static final int PAGE_SIZE = 100;
    private static final int ALL_SLICES = -1;
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Map<Character, Node> rootNodes = new ConcurrentHashMap<>(150);
    private List<Node> wordEndNodes = new LinkedList<>();
    private int size;

    void save(String nodeName) {
        Objects.requireNonNull(nodeName, "Node name is required");
        String finalNodeName = nodeName.toLowerCase();
        rootNodes.computeIfAbsent(finalNodeName.charAt(0),
                character -> {
                    Node node = new Node(character, null);
                    boolean isFinalCharInWord = finalNodeName.length() == 1;
                    if (isFinalCharInWord) {
                        readWriteLock.writeLock().lock();
                        wordEndNodes.add(node);
                        readWriteLock.writeLock().unlock();
                        size++;
                    }
                    node.setLastCharacterInWord(isFinalCharInWord);
                    return node;
                }
        );

        Node processingNode = rootNodes.get(finalNodeName.charAt(0));
        if (nodeName.length() == 1 && processingNode != null) {
            processingNode.incrementStats();
        }
        for (int i = 1; i < finalNodeName.length(); i++) {
            char character = finalNodeName.charAt(i);
            Node subnode = processingNode.getSubnode(character); // Processing subnode cannot be null here as it is
            // calculated if absent
            boolean lastCharacterInWord = finalNodeName.length() - 1 == i;
            if (subnode == null) {
                subnode = new Node(character, processingNode);
                subnode.setLastCharacterInWord(lastCharacterInWord);
                processingNode.add(character, subnode);
            }

            processingNode = subnode;
            if (lastCharacterInWord) {
                readWriteLock.writeLock().lock();
                boolean wordAddedToStorage = wordEndNodes.remove(processingNode);
                readWriteLock.writeLock().unlock();
                if (!wordAddedToStorage) {
                    size++;
                }
                processingNode.setLastCharacterInWord(true);
                processingNode.incrementStats();
                readWriteLock.writeLock().lock();
                wordEndNodes.add(processingNode);
                readWriteLock.writeLock().unlock();
            }
        }
    }

    int getStatsForSlice(int sliceCount) {
        readWriteLock.readLock().lock();
        List<Node> nodes = new ArrayList<>(wordEndNodes);
        readWriteLock.readLock().unlock();
        int result = 0;
        for (Node processing : nodes) {
            result = result + processing.getStatsForSlice(sliceCount);
        }
        return result;
    }

    int getWordStats(String input) {
        Objects.requireNonNull(input);
        String nodeName = input.toLowerCase();
        Node processingNode = rootNodes.get(nodeName.charAt(0));
        if (processingNode == null) return 0;
        if (nodeName.length() == 1) return processingNode.getAllSlicesCount().get();

        for (int i = 1; i < nodeName.length(); i++) {
            char charAt = nodeName.charAt(i);
            Node subnode = processingNode.getSubnode(charAt);
            if (subnode == null) return 0;
            boolean finalCharInWord = nodeName.length() - 1 == i;
            if (finalCharInWord) {
                return subnode.getStatsForSlice(ALL_SLICES);
            }
            processingNode = subnode;
        }
        return 0;
    }

    List<SubredditStatistic> getMostPopularWordsPerSlice(int slices, int page) {
        readWriteLock.readLock().lock();
        List<Node> nodes = wordEndNodes.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getStatsForSlice(slices),
                        o1.getStatsForSlice(slices)))
                .collect(Collectors.toList());
        readWriteLock.readLock().unlock();
        List<SubredditStatistic> result = new ArrayList<>(nodes.size());
        StringBuilder nodeName = new StringBuilder();
        int startElement = (page - 1) * PAGE_SIZE;
        for (int i = 0; i < nodes.size(); i++) {
            if (i < startElement) continue;
            for (int j = i; j < Math.min(nodes.size(), i + PAGE_SIZE); j++) {
                Node processing = nodes.get(j);
                int hitCount = processing.getStatsForSlice(slices);
                while (processing.getParent() != null) {
                    nodeName.append(processing.getLetter());
                    processing = processing.getParent();
                }
                nodeName.append(processing.getLetter());
                result.add(new SubredditStatistic(nodeName.reverse().toString(), hitCount));
                nodeName.setLength(0);
            }
            break;
        }
        return result;
    }

    int getSize() {
        return size;
    }

    void adjustSlices() {
        readWriteLock.writeLock().lock();
        wordEndNodes.parallelStream().forEach(Node::adjustSlice);
        readWriteLock.writeLock().unlock();
    }

    void clear() {
        readWriteLock.writeLock().lock();
        wordEndNodes.clear();
        rootNodes.clear();
        size = 0;
        readWriteLock.writeLock().unlock();
    }

    @EqualsAndHashCode(of = {"letter", "parent"})
    public static class Node {
        private static final int ONE_DAY = 1440;
        @Getter
        private char letter;
        @Getter
        @Setter
        private boolean lastCharacterInWord;
        @Getter
        private AtomicInteger allSlicesCount;
        private AtomicInteger currentSliceCount;
        private Map<Character, Node> subNodes;
        @Getter
        private Node parent;

        private Deque<Integer> timeSlice = new LinkedList<>();

        Node(Character character, Node parent) {
            this.letter = character;
            this.parent = parent;
            this.subNodes = new ConcurrentHashMap<>();
            this.allSlicesCount = new AtomicInteger(0);
            this.currentSliceCount = new AtomicInteger(0);
        }

        void add(Character character, Node child) {
            subNodes.put(character, child);
        }

        Node getSubnode(Character character) {
            return subNodes.get(character);
        }

        void incrementStats() {
            this.currentSliceCount.incrementAndGet();
        }

        int getStatsForSlice(int periodInMinutes) {
            if (periodInMinutes == ALL_SLICES) {
                return currentSliceCount.get() + getAllSlicesCount().get();
            }
            int result = 0;
            int i = periodInMinutes;
            for (Integer integer : timeSlice) {
                result = integer + result;
                i--;
                if (i <= 0) break;
            }
            return result;
        }

        void adjustSlice() {
            allSlicesCount.addAndGet(currentSliceCount.get());
            timeSlice.addFirst(currentSliceCount.get());
            currentSliceCount = new AtomicInteger(0);
            while (timeSlice.size() > ONE_DAY) {
                timeSlice.pollLast();
            }
        }

        @Override
        public String toString() {
            return "Node{" +
                    "letter=" + letter +
                    ", commentAndSubredditCount=" + allSlicesCount +
                    ", currentSlice=" + currentSliceCount +
                    '}';
        }
    }
}