Supermarket Billing System

A console billing application in Java. Customers join a billing queue, prices are looked up in a HashMap, and the last scanned item can be removed using a stack. Every bill is saved to bills.txt.

Data Structures
Structure	Used for
Queue	Billing queue (first come, first served)
HashMap	Price list: item name to price
Stack	Undo the last scanned item
Array	Product catalogue and menu
File handling	Load and save bills in bills.txt

Price list: Rice 60, Milk 30, Bread 40

Features
Add customer
Bill next customer (enter items like Milk x2, Undo removes the last item, Done finishes)
View queue
View saved bills
Exit (saves bills)

Billing is not allowed when the queue is empty.

Run

Requires JDK 11 or later.

bash
javac -d build src/Main.java
cd build
java Main
Test Cases
#	Input	Output
1	Add customer Riya	Riya added to billing queue. Position: 1
2	Bill Riya: Rice x1, Done	Bill for Riya: Rice x1 = 60 | Total 60 and Queue is now empty.
3	Bill: Chocolate	Item Chocolate not found in price list.
4	Queue Riya, Aman. Bill: Milk x2, Bread x1, Undo, Done. View queue	Last item Bread removed. / Bill for Riya: Milk x2 = 60 | Total 60 / 1. Aman
Time Complexity
Operation	Complexity
Add customer (Queue)	O(1)
Price lookup (HashMap)	O(1) average
Scan item / undo (Stack)	O(1)
Build bill	O(K), K = items in cart
View queue	O(C), C = customers waiting
Structure
src/Main.java      Complete solution
tests/             Test inputs
docs/              Report and presentation
