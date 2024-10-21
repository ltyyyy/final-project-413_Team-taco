# Final Project - Team Taco

This is a banking app created as part of our final project. The app provides users with features such as managing contacts, handling transactions, splitting bills, adding favorite users, and viewing public transaction feeds. The frontend is built using React, while the backend is implemented using Java, with MongoDB as the database.

## Demo Video

[Click here to watch the demo video](https://youtu.be/cheeo08x1PA)

## Team Members and Contributions

### Abby Lin
I implemented the `addContact` feature and updated transaction handling for user balances. I introduced new fields in `userDto` to manage contacts and real-time balance updates in `transactionDto`. The backend handlers now properly update and reflect the latest balances for each transaction. The front-end portion of `addContact` is in progress, and I am currently working on refining the transaction table in `HomePage.js` to display the correct user IDs and balances.

### Guillermo Alcantara
I developed the "FavoriteList" feature, allowing users to add other users to their favorites for quick money transfers. The backend includes `FavoriteListDao`, `Dto`, and `FavoriteListHandler`, which manage CRUD operations for the favorite list. I am currently facing issues with connecting the frontend to the backend but will resolve this shortly.

### Chen Yi Chang
I implemented the "split bill" feature, which allows users to divide a transaction equally among multiple people. The backend includes a `SplitHandler` that deducts the correct amounts from the accounts involved. After completing the backend, I connected it with the frontend, where users can activate the split feature via a button. I also enhanced the app's appearance using CSS.

### San-Yu Peng
I implemented the "Public Transaction Feed" feature, allowing users to view a feed of public transactions, similar to Venmo. The frontend includes a toggle button to switch between personal and public transaction views. On the backend, I extended the `PublicTransactionFeedHandler` to securely fetch and display public transaction data.


## Build and Run Instructions

Follow these steps to set up and run the final project on your local machine:

1. **Install MongoDB**:
   - Visit the [MongoDB Documentation](https://www.mongodb.com/zh-cn/docs/manual/administration/install-community/) and follow the instructions to install MongoDB based on your operating system.

2. **Run the Backend Server**:
   - Navigate to the `/back-end/src/main/java/server` directory.
   - Open and run the `Server.java` file in your IDE.

3. **Install Frontend Dependencies**:
   - Open your terminal and navigate to the `/front-end` directory.
   - Run the following command:
     ```
     npm install
     ```

4. **Run the Frontend Application**:
   - In the same terminal session, execute:
     ```
     npm run
     ```
   - The frontend server will start, and you can access the application.

5. **Explore the Application**:
   - Open your web browser and go to [http://localhost:3000](http://localhost:3000) to start exploring the app!
