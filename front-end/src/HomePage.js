import React from 'react';
import { Navigate } from 'react-router-dom';
import './HomePage.css';

export default function HomePage() {

    const [ammount, setAmmount] = React.useState('');
    const [transactions, setTransactions] = React.useState([]); //get transactions
    const [ToUser, setToUser] = React.useState('');
    const [userContact, setUserContact] = React.useState([]); //get contacts
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const currentUserId = localStorage.getItem('currentUserId');
    const [showPublicTransactions, setShowPublicTransactions] = React.useState(false);
    const [searchQuery, setSearchQuery] = React.useState(''); 

    const [popupMessageTransaction, setPopupMessageTransaction] = React.useState('');
    const [popupTypeTransaction, setPopupTypeTransaction] = React.useState('');
    const [popupVisibleTransaction, setPopupVisibleTransaction] = React.useState(false);

    const [popupMessageContact, setPopupMessageContact] = React.useState('');
    const [popupTypeContact, setPopupTypeContact] = React.useState('');
    const [popupVisibleContact, setPopupVisibleContact] = React.useState(false);


    function showPopup(message, type, section) {
        if (section === 'transaction') {
            setPopupMessageTransaction(message);
            setPopupTypeTransaction(type + ' popup-fade-in');
            setPopupVisibleTransaction(true);

            setTimeout(() => {
                setPopupTypeTransaction(type + ' popup-fade-out');
            }, 1500);
            setTimeout(() => {
                setPopupVisibleTransaction(false);
            }, 2200);
        } else if (section === 'contact') {
            setPopupMessageContact(message);
            setPopupTypeContact(type + ' popup-fade-in');
            setPopupVisibleContact(true);

            setTimeout(() => {
                setPopupTypeContact(type + ' popup-fade-out');
            }, 1500);
            setTimeout(() => {
                setPopupVisibleContact(false);
            }, 2200);
        }
    }

    

    function updateAmmount(event) {
        const numberValue = Number(event.target.value);
        if (isNaN(numberValue)) {
            return;
        }
        setAmmount(event.target.value);
    }

    function updateUser(event) {
        setToUser(event.target.value);
    }
    
    function selectContact(contact){
        setToUser(contact);
        setSearchQuery('');
    }

    function searchForContact(event) {
        setSearchQuery(event.target.value);
    }

    function searchOrAdd() {
        if (!searchQuery.trim()) {
            return;
        }
    
        const contactExists = userContact.some(contact => 
            contact.contacts.some(contactUser => contactUser === searchQuery)
        );
    
        if (contactExists) {
            addNewContact();
            fetchContacts();  
        } else {
            addNewContact();
            fetchContacts();  
        }
    }
    

    const filteredContacts = userContact.flatMap(contact => 
        contact.contacts.filter(contactUser => 
            contactUser.toLowerCase().includes(searchQuery.toLowerCase())
        )
    );

    function addNewContact() {
        const userDto = {
            contacts: [searchQuery] 
        };
    
        const options = {
            method: 'POST',
            body: JSON.stringify(userDto),
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
        };
    
        fetch('/createContact', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                if (apiRes.status) { 
                    setSearchQuery('');
                    showPopup(apiRes.message, 'popup-success','contact');
                } else {
                    setSearchQuery('');
                    showPopup(apiRes.message, 'popup-error','contact');
                }
                fetchContacts();  
            })
            .catch((error) => {
                console.error('Error adding new contact:', error);
            });
    }

    function deleteContact(contactUserName) {
        const userDto = {
            contacts: [contactUserName]
        };

        const options = {
            method: 'POST',
            body: JSON.stringify(userDto),
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
        };

        fetch('/deleteContact', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                // Update contact
                setUserContact(prevContacts => prevContacts.flatMap(contact => ({
                    contact,
                    contacts: contact.contacts.filter(user => user !== contactUserName)
                })));
                setSearchQuery('');
                fetchContacts();  
                showPopup(apiRes.message, 'popup-success','contact');
                
            })
            .catch((error) => {
                console.error('Error deleting contact:', error);
            });
    }

      
    


    function split() {
        const transactionDto = {
            amount: Number(ammount),
            toId: ToUser,
            userBalance: Number(ammount)
        };
    
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
        };
    
        fetch('/split', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setAmmount('');
                setToUser('');
                fetchTransaction();
            })
            .catch((error) => {
                console.log(error);
            });
    }

  
    function deposit() {
        const transactionDto = {
            amount: Number(ammount),
            userBalance: Number(ammount)
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            credentials: 'include',
        };
        fetch('/createDeposit', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setAmmount('');
                fetchTransaction();

                if(apiRes.status){
                showPopup(apiRes.message, 'popup-success','transaction');
                }else{
                showPopup(apiRes.message, 'popup-error','transaction');
                }
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }


    function transfer() {
        const transactionDto = {
            amount: Number(ammount),
            toId: ToUser,
            userBalance: Number(ammount)
        };

        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
        };

        fetch('/transfer', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setAmmount('');
                setToUser('');
                fetchTransaction();
                if(apiRes.status){
                showPopup(apiRes.message, 'popup-success', 'transaction');
                }else{
                showPopup(apiRes.message, 'popup-error', 'transaction');
                }
                
            })
            .catch((error) => {
                console.log(error);
            });
    }


    async function withdraw() {
        const transactionDto = {
            amount: Number(ammount),
            userBalance: Number(ammount)
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
        };
    
        fetch('/withdraw', options)
            .then(response => response.json())
            .then(apiRes => {
                setAmmount('');
                fetchTransaction();
                if (apiRes.status) {
                    showPopup(apiRes.message, 'popup-success', 'transaction');
                } else {
                    showPopup(apiRes.message, 'popup-error', 'transaction');
                }
            })
            .catch(error => {
                console.error('Error during withdrawal:', error);
            });
    }
    

    React.useEffect(() => {
        // triggers when componenet mounds
        // https://react.dev/reference/react/useEffect
        // fetching data
        // https://developer.mozilla.org/en-US/docs/Web/API/fetch
        fetchContacts();
        fetchTransaction();
        if (showPublicTransactions) {
            fetchPublicTransactionFeed();
        }
    }, [showPublicTransactions]);

    function fetchTransaction() {
        fetch('/getTransactions')
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                // will see transactions here as they come
                setTransactions(apiRes.data.map(transaction => ({
                    ...transaction,
                    uniqueId: transaction.uniqueId
                })));
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    function fetchContacts() {
        fetch('/getContacts')
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setUserContact(apiRes.data);
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    function fetchPublicTransactionFeed() {
        fetch('/publicTransactionFeed')
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setTransactions(apiRes.data);
            })
            .catch((error) => {
                console.log(error);
            });
    }

    function togglePublicTransactions() {
        setShowPublicTransactions(!showPublicTransactions);
        setTransactions([]);
    }


    // redirect
    if (shouldRedirect) {
        return <Navigate to="/" replace={true} />;
    }

    function logout() {
        setShouldRedirect(true);
    }

    return (
     
        <div class='background'>
            
            <header>
                 <h1>Bank of Taco</h1>
            </header>
            <div>
            Welcome User: {currentUserId}
            <br></br><br></br>
             Amount:  <input placeholder='Amount...' value={ammount} onChange={updateAmmount} />
            <br></br>
             To User: <input placeholder='Username...' value={ToUser} onChange={updateUser}/>
             <br></br>
            <button onClick={deposit}>Deposit</button>
            <button onClick={withdraw}>Withdraw</button>
            <button onClick={transfer}>Transfer</button>
            <button onClick={split}>Split</button>
            <button class='logout' onClick={logout}>Logout</button>
            </div>

            <details>
            <summary>Click to show My Contacts</summary>
            <div className="search-container">
                <input
                    type="text"
                    placeholder="Type to search or add a contact"
                    value={searchQuery}
                    onChange={searchForContact}
                />
                <button onClick={searchOrAdd}>Add</button>
                {popupVisibleContact && (
                    <div className={popupTypeContact}>{popupMessageContact}</div>
                )}

                <table>
                    <thead>
                        <tr>
                            <th>My Contact (Click to set as recipient)</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredContacts.map((contactUser, index) => (
                            <tr key={index} onClick={() => selectContact(contactUser)}>
                                <td className="contact-cell">
                                    {contactUser}
                                    <button
                                    className="delete-button"
                                    onClick={(event) => {
                                        event.stopPropagation(); 
                                        deleteContact(contactUser);
                                    }}
                                    >
                                    Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            </details>

            <br></br>

            <button class='publicTrans'  onClick={togglePublicTransactions}>Personal/Public Transactions</button>
            
            {popupVisibleTransaction && (
            <div className={popupTypeTransaction}>{popupMessageTransaction}</div>
            )}


            <table>
                <thead>
                    <tr>
                        <th>
                            Type
                        </th>
                        <th>
                            Amount
                        </th>
                        <th>
                            ID
                        </th>
                        <th>
                            From user
                        </th>
                        <th>
                            To user
                        </th>
                        <th>
                            Balance
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {transactions.map((transaction) => (
                        <tr>
                            <td>{transaction.transactionType}</td>
                            <td>{transaction.amount}</td>
                            <td>{transaction.uniqueId}</td>
                            <td>{transaction.userId}</td>
                            <td>{transaction.toId}</td>
                            <td>
                                {currentUserId === transaction.userId
                                    ? transaction.userBalance
                                    : transaction.toUserBalance}
                            </td>

                        </tr>

                    ))}

                </tbody>

            </table>

            <footer>
                Team Taco © Fall 2023
            </footer>

        </div>
    );
}