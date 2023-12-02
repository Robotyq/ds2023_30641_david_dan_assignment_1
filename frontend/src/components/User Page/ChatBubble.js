import React, {useState} from 'react';
import axios from 'axios';
import Chat from "./Chat";
import {HOST} from "../../commons/hosts";

const SupportChatBubble = () => {
    const [chatId, setChatId] = useState(sessionStorage.getItem('chatId'));
    // const [showChatBubble, setShowChatBubble] = useState(!!chatId);
    const [isChatStarted, setIsChatStarted] = useState(!!chatId);

    // useEffect(() => {
    //     // alert("SupportChatBubble.js")
    //     const initializeWebSocket = () => {
    //         // Initialize WebSocket connection using chatId as topic name
    //         if (chatId) {
    //             const webSocket = new WebSocket(`ws://your-websocket-server/${chatId}`);
    //
    //             webSocket.addEventListener('message', (event) => {
    //                 // Handle incoming messages from WebSocket
    //                 console.log('WebSocket message received:', event.data);
    //             });
    //
    //             // Add cleanup logic if needed
    //             return () => {
    //                 webSocket.close();
    //             };
    //         }
    //     };
    //
    //     if (chatId) {
    //         initializeWebSocket();
    //     }
    // }, [isChatStarted, chatId]);

    const handleStartChat = async () => {
        let loggedUser = JSON.parse(sessionStorage.getItem('loggedUser'))
        const newChatId = loggedUser.id;
        setChatId(newChatId);
        setIsChatStarted(true);
        // Call the backend API to start the chat and get a new chatId
        try {
            const response = await axios.get(HOST.monitoring_api + 'start-chat/' + loggedUser.id);
            console.log(response.data)
            // Update chatId in session storage and state
            sessionStorage.setItem('chatId', newChatId);
        } catch (error) {
            alert('Error starting chat:' + error + '\n Try refreshing the page and starting again.');
        }
    };

    // const handleBubbleClick = () => {
    //     // Toggle the visibility of the chat bubble
    //     setShowChatBubble(!showChatBubble);
    // };

    return (
        <>
            {isChatStarted ? (
                // Render your chat bubble UI here
                <div className="chat-bubble"
                    // onClick={handleBubbleClick}
                >
                    <Chat chatId={chatId}>
                    </Chat>
                </div>
            ) : (
                // Render the start chat message
                <div className="start-chat-message" onClick={handleStartChat}>
                    Admin Chat
                </div>
            )}

            <style>
                {`
          .start-chat-message {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4db3f7;
            color: #fff;
            border: none;
            border-radius: 105px;
            cursor: help;
            text-align: center;
            text-decoration: none;
            font-size: 16px;
            transition: background-color 0.4s ease;
          }

          .start-chat-message:hover {
            background-color: #0210a8;
          }
        `}
            </style>
        </>
    );
};

export default SupportChatBubble;
