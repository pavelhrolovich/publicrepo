#TASK
You have a carpet with holes. You have to find the largest possible carpet part without hole.
Use Kotlin as a language

Example:
Carpet size:
210x297.5

Holes:
90, 100
120, 130
150, 143.5

Return:
210.0 x 154

#SOLUTION
1. Take 1st hole
2. Cut carpet into 4 pieces (2 horizontal and 2 vertical)
3. Check every piece for hole
4. If hole exists, repeat step 2-4 for each piece
5. For each part calculate area and sort parts in descending order
6. Find first piece without holes
