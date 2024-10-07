import React from 'react';
import { Navigate } from 'react-router-dom';
import './LoginPage.css';

export default function Login() {
    const [userName, setUserName] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const [popupVisible, setPopupVisible] = React.useState(false);
    const [popupMessage, setPopupMessage] = React.useState('');
    const [popupType, setPopupType] = React.useState('');

    function updateUserName(event) {
        setUserName(event.target.value);
    }

    function updatePassword(event) {
        setPassword(event.target.value);
    }

    function showPopup(message, type) {
        setPopupMessage(message);
        setPopupType(type + ' popup-fade-in');
        setPopupVisible(true);
    
        // Start fade-out effect after a delay
        setTimeout(() => {
            // Change class to start fade-out animation
            setPopupType(type + ' popup-fade-out');
        }, 1500); // Time for message to stay visible before fading out
    
        // Hide the popup after the fade-out effect completes
        setTimeout(() => {
            setPopupVisible(false);
        }, 2200); // Total duration including fade-in, visible time, and fade-out
    }
    
    

    function register() {
        console.log('Registering ' + userName + ' ' + password);
        const userDto = {
            userName: userName,
            password: password
        };

        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userDto)
        };

        fetch('/createUser', options)
            .then((res) => res.json())
            .then((apiRes) => {
                if (apiRes.status) {
                    setUserName('');
                    setPassword('');
                    showPopup(apiRes.message, 'popup-success');
                } else {
                    showPopup(apiRes.message, 'popup-error');
                }
            })
            .catch((error) => {
                console.error(error);
                showPopup('Failed to register', error);
            });
    }

    function logIn() {
        console.log('Logging in ' + userName + ' ' + password);
        const userDto = {
            userName: userName,
            password: password
        };

        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userDto)
        };

        fetch('/login', options)
            .then((res) => {
                if (res.ok) {
                    return res.json();
                }
                return res.json().then(json => Promise.reject(json));
            })
            .then((apiRes) => {
                localStorage.setItem('currentUserId', userName);
                setShouldRedirect(true);
            })
            .catch((error) => {
                console.error(error);
                showPopup(error.message || 'Failed to log in', 'popup-error');
            });
    }

    if(shouldRedirect){
        return <Navigate to="/home" replace={true} />;
    }

    return (
        <div>
            <header>
                <h1>Login To Bank of Taco</h1>
            </header>

            <div>
                Username: <input placeholder='Username...' value={userName} onChange={updateUserName} />
                <br />
                Password: <input placeholder='Password...' type="password" value={password} onChange={updatePassword} />
            </div>

            <div>
                <button onClick={register}>Register</button>
                <button onClick={logIn}>Login</button>
            </div>

            {popupVisible && (
             <div className={popupType}>                  
             {popupMessage}
                </div>
            )}

            <footer>
                Team Taco © Fall 2023
            </footer>
        </div>
    );
}
