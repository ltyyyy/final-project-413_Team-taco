# Final Project

Demo Video Link:
https://youtu.be/cheeo08x1PA

Abby Lin

	I implemented an `addContact` feature and updated transaction handling for user balances. The backend now accommodates a `contacts` field within `userDto`, which is a string list holding a user's contacts. Utilizing the `CreateContactHandler`, users can add contacts provided the contact's username exists in the database while users cannot add themselves as a contact, and the `getContactHandler` allowing users to get their contact list. Additionally, I introduced `userBalance` and `toUserBalance` fields in `transactionDto`, allowing for real-time balance updates within each transaction. In the backend handlers for withdrawal, transfer, and deposits, these balance fields are set accordingly to reflect the most current balance after each transaction.

	Initially, my strategy was to source the balance information directly from `userDto`. However, this proved impractical as balances are not static and change with each transaction. Given that the `HomePage.js` relies on `transactionDto` to populate transaction details such as user IDs and transaction amounts, I opted to incorporate the balance fields directly into `transactionDto`. This decision effectively solved the problem of tracking and displaying updated balance information after each transaction.

	The front-end portion of the `addContact` function is still in progress. The next steps will involve ensuring the UI is user-friendly and integrates seamlessly with the backend logic. Furthermore, I need to refine the transaction table in `HomePage.js` to display the correct user IDs and handle the newly incorporated balance data accurately. By next week, I aim to have these front-end features fully functional and thoroughly tested to ensure they match the backend's capabilities.


 Guillermo Alcantara
 
I implemented the "FavoriteList" feature onto our app which would allow users to be able to add other users to their favorites and have them as a quick access to send money. In the backend I added three files: a FavoriteListDao, Dto, and a FavoriteListHandler. The Dto allows users to create, read, update, and delete users from the favorite list. This was my initial plan and is still a work in progress because as of right now I am having issues with the frontend and connecting my features.

 A difficulty that I'm having at the moment is with an error code that I'm getting when testing out features on the frontend like registering users I have talked to my teammates and they are not having this issue. For them its all working fine, I will try to add my frontend portion that will hopefully connect my backend feature and then hopefully try and figure out the issue with me testing out the register portion.

 The error code Im getting is a "SLF4J: failed to load class", "SLF4J: Defaulting To no-operation logger implementation"

 

Chen Yi Chang

I added the "spilt bill" feature to the banking app. I began the project by adding and completing some basic functions such as withdrawal, deposit, transfer, and log in. Later, I added a split bill function to our banking system so that users could deduct the amount in an even distribution based on the number of people. In my backend, I added a new Split Handler to get the amount and object via splitDto, and the amount obtained was divided equally and deducted from the database. Finally, on the front end, I added a button and dispatch function to activate the handler. In addition, after all functions were completed, I improved the app's appearance by using the newly created CSS file.

The first issue I ran into was that I didn't know how to connect the front and back ends, which caused me to waste a lot of time trying to figure it out. But then I realized that I needed to start the Server before I could test my local host. Another issue is that when I try to merge other team members' code, I discover that our code sometimes conflicts. When I am editing HomePage.js, for example, my teammates push their new code, and when I try to pull it into mine, the directory files will have merge conflicts. I spent a lot of time on these disagreements.

San-Yu Peng

I implemented the "Public Transaction Feed" feature. Generally speaking, each person's account can only see their own transaction records. But this feature allows users to view a feed of public transactions, which is a relatively rare feature I’ve only seen on Venmo. The approach taken involved modifying the React frontend to include a toggle button "Personal/Public Transactions" that allows users to switch between personal transaction history and the newly added public transaction source. I also implemented a new function "fetchPublicTransactionFeed" that fetches and displays public transactions by making a GET request to the appropriate backend endpoint "/publicTransactionFeed". The backend "PublicTransactionFeedHandler" has also been extended to handle this specific functionality, ensuring secure and controlled access to public transaction data.

At the beginning, because I could only see the user's own transaction records, when I only created a function at the beginning, an error occurred on one side. An error occurs when trying to switch to all records, resulting in inconsistent front-end and back-end behavior. I later created a dedicated function specifically for viewing all user records, ensuring a consistent approach on both sides of the application.

Guillermo Alcantara
