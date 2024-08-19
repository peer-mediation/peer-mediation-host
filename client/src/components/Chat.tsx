import React from 'react';
import "../styles/Chat.css"
import { useState, useEffect } from 'react';
import {addWord, getWords, addCookie} from "../utils/api"
import { getLoginCookie } from '../utils/cookie';

export interface adminData {
    isAdmin: boolean;
    uid: string | null;
}

/**
 * This is the Chat component of this application. This creates a chatbox that allows users to
 * communicate with each other. All message history is loaded from the backend, and users can send
 * new messages via the text input box. 
 * @param props the props are used to determine if the user should be considered an admin or not
 * upon rendering
 * @returns 
 */
const Chat: React.FunctionComponent<adminData> = (props) => {
    const [messages, setMessages] = useState<string[]>([]);

    const USER_ID = (props.uid === null) ? getLoginCookie() || "" : props.uid;

    //use effect hook to get the messages from the backend and set the state variable
    useEffect(() => {
        getWords(USER_ID).then((data) => {
            setMessages(data.words)
        });
    }, []);

    //function designed to make the chat box scroll to the bottom upon rendering
    const scroll = () => {
        let scrollBox = document.getElementById("text-scroll");
                        
        if (scrollBox) {
            scrollBox.scrollTop = scrollBox.scrollHeight;
        }
    };

    //function to send the message that the user inputs to the backend and display the message
    //on the frontend
    const handleSendMessage = async (newMessage: string) => {
        if (props.isAdmin) {
            newMessage = "Peer Mediator: " + newMessage;
        } else {
            newMessage = "Student User: " + newMessage;
        }
        setMessages([...messages, newMessage]);

        if (props.uid === null) {
            await addCookie(USER_ID);
        } 
        await addWord(USER_ID, newMessage);
    };

    return (
        <div className="chat-page">
            <h1>Chat Page</h1>
                <div className="text-scroll" id="text-scroll" aria-label="text-scroll">
                    {messages.map((message, index) => {
                        let backgroundColor;

                        if (message.includes("Peer Mediator")) {
                            backgroundColor = '#007bff';
                        } else {
                            backgroundColor = '#38cba0';
                        }

                        scroll();

                        return (
                            <div aria-label="message" key={index} className="message" style={{ backgroundColor: backgroundColor }}>
                                {message}
                            </div>
                        );
                    })}
                </div>
            <div className="form-group" aria-label="form-group">
                <label htmlFor="message">Message: </label>
                <input
                    aria-label="Type message here"
                    placeholder='Type message here'
                    type="text"
                    id="message"
                    onKeyDown={(e) => {
                        if (e.key === "Enter") {
                            document.getElementById('send-button')?.click()
                        }
                    }}
                />
                <button
                    aria-label="send-button"
                    className="send-button"
                    id="send-button"
                    onClick={() => {
                        const newMessage = (document.getElementById("message") as HTMLInputElement).value;
                        if (newMessage === "") {
                            return
                        }
                        handleSendMessage(newMessage);
                        var temp = document.getElementById("message") as HTMLInputElement;
                        temp.value = "";
                        scroll();
                    }}
                >Send</button>
            </div>
        </div>
    );
};

export default Chat;