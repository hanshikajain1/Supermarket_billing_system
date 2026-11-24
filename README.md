<div align="center">
🛒 Supermarket Billing System

A console billing app in Java, powered by a Queue, a HashMap, a Stack and an Array.

Show Image Show Image Show Image Show Image

Project PS 12 · Data Structures and Algorithms · Rungta International Skills University

</div>
📖 Table of contents
About the project
Data structures at a glance
Features
How it works
Getting started
Sample session
Test cases
Time complexity
Project structure
Future scope
Author
📌 About the project

At a supermarket counter, three things must go right: customers are served in order, item prices are found instantly, and a scanning mistake can be undone without restarting the bill.

This project solves each of those with the data structure that fits it best:

Real-world need	Data structure
Serve customers first-come, first-served	Queue
Find an item's price instantly	HashMap
Remove the most recently scanned item	Stack
Store the product catalogue and menu	Array
Keep bills after the program closes	File handling

The whole solution lives in one file, src/Main.java, and uses only the standard Java library.

🧩 Data structures at a glance
Structure	In the code	Operations used	Why this one
Queue (LinkedList)	billingQueue	offer(), peek(), poll()	FIFO keeps the line fair
HashMap	priceList	put(), get()	O(1) average price lookup
Stack	cartStack	push(), pop()	LIFO makes "undo last item" natural
Array	PRODUCT_NAMES, PRODUCT_PRICES, MENU	indexed access	Fixed catalogue that fills the HashMap
File I/O	loadBills(), saveBills()	BufferedReader, PrintWriter	Bills survive restarts

Price list: Rice ₹60 · Milk ₹30 · Bread ₹40

✨ Features
Menu	Method	What it does
1	addCustomer()	Adds a customer to the back of the queue and shows their position
2	billNextCustomer()	Bills the customer at the front. Refuses if the queue is empty
2 → Undo	undoLastItem()	Removes the last scanned item
3	viewQueue()	Lists waiting customers, front to back
4	viewSavedBills()	Shows every saved bill, including ones from earlier runs
5	saveBills()	Saves bills to bills.txt and exits
Smart input handling
Enter items as Milk x2, Milk 2 or just Milk (quantity defaults to 1).
Item names are not case-sensitive: rice, RICE and Rice all work.
Unknown items, zero quantities and invalid menu choices are rejected with a clear message.
A customer is removed from the queue only after their bill is made, so nobody is lost by mistake.

🚀 Getting started
Requirements
JDK 11 or later (check with java -version)
Run it
bash
# 1. Clone the repository
git clone https://github.com/hanshikajain1/Supermarket_billing_system.git
cd Supermarket_billing_system

# 2. Compile
javac -d build src/Main.java

# 3. Run (from the build folder, so bills.txt is created there)
cd build
java Main

Tip: with JDK 11+ you can also run directly with java src/Main.java.

Run the ready-made tests
bash
sh tests/run_tests.sh
💻 Sample session
text
Choice: 1
Name: Riya
Riya added to billing queue. Position: 1

Choice: 1
Name: Aman
Aman added to billing queue. Position: 2

Choice: 3
1. Riya
2. Aman

Choice: 2
Billing: Riya
Item: Milk x2
Added Milk x2
Item: Bread x1
Added Bread x1
Item: Undo
Last item Bread removed.
Item: Chocolate
Item Chocolate not found in price list.
Item: Rice
Added Rice x1
Item: Done
Bill for Riya: Milk x2 = 60, Rice x1 = 60 | Total 120

Choice: 3
1. Aman

Choice: 4
1) Bill for Riya: Milk x2 = 60, Rice x1 = 60 | Total 120

Choice: 5
Bills saved to bills.txt. Goodbye!

Run it again and option 4 still shows Riya's bill, loaded from bills.txt.

✅ Test cases
#	Scenario	Input	Expected output
1	Normal	Menu 1, name Riya	Riya added to billing queue. Position: 1
2	Boundary	Only Riya in queue; menu 2; Rice x1; Done	Bill for Riya: Rice x1 = 60 | Total 60 then Queue is now empty.
3	Invalid	Menu 2; item Chocolate	Item Chocolate not found in price list.
4	Different valid	Queue Riya, Aman; menu 2; Milk x2, Bread x1, Undo, Done; menu 3	Last item Bread removed. · Bill for Riya: Milk x2 = 60 | Total 60 · 1. Aman
5	Empty queue	Menu 2 with nobody waiting	Queue is empty. Cannot bill.

The input for each case is in the tests/ folder.

⏱️ Time complexity

K = items in one cart · C = customers waiting

Operation	Structure	Time
Add customer	Queue.offer()	O(1)
Serve customer	Queue.poll()	O(1)
Price lookup	HashMap.get()	O(1) average
Scan item	Stack.push()	O(1)
Undo last item	Stack.pop()	O(1)
Build bill	Traverse cart	O(K)
View queue	Traverse queue	O(C)
📁 Project structure
text
Supermarket_billing_system/
├── src/
│   └── Main.java          # Complete solution (single file)
├── tests/
│   ├── tc1_input.txt      # Test inputs
│   ├── ...
│   └── run_tests.sh       # Runs every test input
├── docs/
│   ├── Interim_Report.docx
│   └── Presentation.pptx
├── .gitignore
└── README.md

bills.txt is created automatically the first time you bill a customer.

🔮 Future scope
GST, discounts and coupons
Stock tracking with low-stock alerts
A graphical interface with Java Swing or JavaFX
A database such as MySQL instead of a text file
Multiple billing counters, each with its own queue
Daily and monthly sales reports
👩‍💻 Author

Hanshika Jain ERP ID 10543 · B.Tech CSE (AI & ML) in association with Google Rungta International Skills University, Bhilai, Chhattisgarh

Guide: Ms. Naina Singh

<div align="center">

If you found this project useful, please ⭐ the repository.

</div>
