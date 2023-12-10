// Import necessary dependencies
import React, {useEffect, useState} from 'react';
import SockJsClient from 'react-stomp';
import {HOST} from "../../commons/hosts";
import './Chat.css';

// Define the Chat component
const Chat = (props) => {
    // Function to get initial messages from sessionStorage or use default
    const getInitialMessages = () => {
        const storedMessages = sessionStorage.getItem('messages');
        const initialMesaage = {
            content: "Please wait for an Admin to join this conversation",
            sender: 'system',
        }
        return storedMessages ? JSON.parse(storedMessages) : [initialMesaage];
    };

    const [messages, setMessages] = useState(getInitialMessages);
    const [newMessage, setNewMessage] = useState('');
    const [isTyping, setIsTyping] = useState(false);
    const [seen, setSeen] = useState(false);
    const chatId = props.chatId;
    let clientRef;
    let typingTimer = Date.now();

    const handleTyping = () => {
        if (!isTyping) {
            setIsTyping(true);
            // Clear the typing status after 1 second
            setTimeout(() => {
                setIsTyping(false);
            }, 3000);
        }
    };

    useEffect(() => {
        if (typingTimer + 1 > Date.now()) {
            clientRef.sendMessage('/app/userChat/' + chatId, 'TYPE');
            typingTimer = Date.now();
        }
    }, [newMessage]);
    const handleConnect = () => {
        console.log('Connected to the Chat WebSocket server. Topic: ' + '/topic/user/' + chatId);
    };

    const handleDisconnect = () => {
        console.log('Disconnected from the Chat WebSocket server');
    };

    const handleMessage = (message) => {
        if (message === 'TYPE') {
            handleTyping();
            setSeen(true)
            return;
        }
        if (message === 'SEEN') {
            setSeen(true)
            return;
        }
        setMessages([...messages, message]);
    };

    const sendMessage = () => {
        const message = {
            content: newMessage,
            sender: 'user',
        };
        clientRef.sendMessage('/app/userChat/' + chatId, JSON.stringify(message));
        setNewMessage('');
        setSeen(false)

    };

    useEffect(() => {
        let newMessages = JSON.stringify(messages);
        sessionStorage.setItem('messages', newMessages);
    }, [messages]);

    function handleKeyPress(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    }

    return (
        <div onClick={() => {
            clientRef.sendMessage('/app/userChat/' + chatId, 'SEEN')
        }}>
            <div>
                <ul>
                    {messages.map((message, index) => (
                        <li
                            key={index}
                            style={{
                                listStyle: 'none',
                                textAlign: message.sender === 'user' ? 'right' : 'left',
                                marginLeft: message.sender === 'user' ? '0' : '1em', // Adjust the margin as needed
                                color: message.sender === 'user' ? 'blue' : 'red',
                            }}
                        >
                            {message.content}
                        </li>
                    ))}
                </ul>
            </div>
            <div className="status-container">
                {/* Typing animation (invisible instead of disappearing) */}
                {isTyping && <div className="typing-animation">Typing...</div>}
                {/* SEEN animation (on the right side) */}
                {seen && <div className="seen-animation">SEEN</div>}
            </div>
            <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyPress={(e) => handleKeyPress(e)}
            />
            <button onClick={sendMessage}>Send</button>
            <SockJsClient
                url={HOST.chat_socket}
                topics={['/topic/user/' + chatId]}
                onConnect={handleConnect}
                onDisconnect={handleDisconnect}
                onMessage={handleMessage}
                ref={(client) => (clientRef = client)}
            />
        </div>
    );
};

export default Chat;
