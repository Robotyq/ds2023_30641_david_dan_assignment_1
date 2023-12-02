import React, {useEffect, useState} from 'react';
import SockJsClient from 'react-stomp';
import {HOST} from "../../commons/hosts";
import {Button, Input} from "reactstrap";
import './Chat.css';

const AdminChat = () => {
    const [chats, setChats] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [currentChat, setCurrentChat] = useState(null);
    const [typing, setTyping] = useState(false);
    let typingTimer = Date.now();

    useEffect(() => {
        // Your WebSocket setup code, assuming you have a socketRef
        if (currentChat === null) {
            return null;
        }
        if (typingTimer + 1 > Date.now()) {
            // return null;
        }
        const socketRef = currentChat.socketRef;
        socketRef.sendMessage('/app/adminChat/' + currentChat.chatId, 'TYPE');
        typingTimer = Date.now();
    }, [newMessage]);

    useEffect(() => {
        if (currentChat === null) {
            return null;
        }
        const socketRef = currentChat.socketRef;
        socketRef.sendMessage('/app/adminChat/' + currentChat.chatId, 'SEEN');
    }, [currentChat]);

    const handleNewChat = (message) => {
        // Extract chatId from the message
        const chatId = message.split('user:')[1];
        if (chatId) {
            // Add a new chat for the user
            setChats(prevChats => [...prevChats, {
                chatId: chatId, messages: [{
                    content: "Please wait for an Admin to join this conversation",
                    sender: 'system',
                    socketRef: null,
                    lastType: new Date().setTime(10),
                    seen: false,
                }]
            }]);
            console.log(chats)
        }
    };

    const handleIncomingMessage = (message, topic) => {
        console.log('received message:\n')
        console.log(message)
        // Extract chatId from the topic
        const chatId = topic.split('/')[3];
        if (chatId) {

            // Update the chat with the incoming message
            const chatIndex = chats.findIndex(chat => chat.chatId === chatId);
            if (message === 'TYPE') {
                chats[chatIndex].lastType = new Date();
                setTyping(true);
                chats[chatIndex].seen = true;
                setChats([...chats]);
                setTimeout(() => {
                    setTyping(false);
                }, 2000)
                return;
            }
            if (message === 'SEEN') {
                chats[chatIndex].seen = true;
                setChats([...chats]);
                return;
            }

            chats[chatIndex].messages.push(message);
            setChats([...chats]);
        }
    };

    const handleSendMessage = () => {
        if (currentChat) {
            console.log('currentChat')
            console.log(currentChat)
            let socketRef = currentChat.socketRef
            let chatId = currentChat.chatId
            let message = {
                content: newMessage,
                sender: 'admin',
            }
            socketRef.sendMessage('/app/adminChat/' + chatId, JSON.stringify(message));
            currentChat.seen = false;
            // Clear the input field after sending the message
            setNewMessage('');
        } else {
            alert("Please select a destination user first")
        }
    };

    return (
        <div>
            <h1>Admin Page</h1>

            <div>
                {chats.map(chat => (
                    <div key={chat.chatId}>
                        <h3 onClick={() => setCurrentChat(chat)}>
                            Chat: {chat.chatId.split('-')[0]} {currentChat === chat && '(Selected)'}
                        </h3>
                        <ul onClick={() => setCurrentChat(chat)}>
                            {chat.messages.map((message, index) => (
                                <li key={index} style={{
                                    listStyle: 'none',
                                    textAlign: message.sender !== 'user' ? 'right' : 'left',
                                    marginLeft: message.sender !== 'user' ? '0' : '3em', // Adjust the margin as needed
                                    marginRight: message.sender === 'user' ? '0' : '3em', // Adjust the margin as needed
                                    color: message.sender !== 'user' ? 'blue' : 'red',
                                }}>{message.content}</li>
                            ))}
                        </ul>
                        {/* Typing animation (invisible instead of disappearing) */}
                        {typing && chat.lastType.valueOf() + 1000 > new Date().valueOf() &&
                            <div className="typing-animation">Typing...</div>}
                        {/*{true && <div className="typing-animation">Typing...</div>}*/}
                        {/* SEEN animation (on the right side) */}
                        {chat.seen && <div className="seen-animation">SEEN</div>}
                        {currentChat === chat && (
                            <div>
                                <Input
                                    type="text"
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                />
                                <br/>
                                <Button color="primary" onClick={() => handleSendMessage()}>
                                    Send Message
                                </Button>
                                <br/>
                                <br/>
                                <br/>
                            </div>
                        )}
                    </div>
                ))}
            </div>
            <SockJsClient
                url={HOST.monitoring_socket}
                topics={['/topic/admin']}
                onConnect={() => console.log('Connected')}
                onDisconnect={() => console.log('Disconnected')}
                onMessage={(message) => handleNewChat(message)}
            />
            {chats.map(chat => (
                <SockJsClient
                    key={chat.chatId}
                    url={HOST.monitoring_socket}
                    topics={['/topic/admin/' + chat.chatId]}
                    onMessage={(message, topic) => handleIncomingMessage(message, topic)}
                    ref={(client) => (chat.socketRef = client)}
                />
            ))}
        </div>
    );
};

export default AdminChat;
