http://www.youtube.com/watch?v=h9isfA69a1U

I made this thing, took maybe 3 days of work. It's a program where you can run around, like in an online game, the position and movement is transfered over internet. It's written in Java.

Over the character is a white label, in it a unique ID which the server sends to every client. So every connected client has a unique ID, stored in the server and the clients can keep track of all the connected clients. The server is just like a echo server, so if a client sends a message to the server, the server sends that message to all the other connected clients with the ID attached to it.

I thought that I cannot keep sending positions repeatedly, because it would overload the server if I had many connected clients. So I thought of having a 500ms Timer, which records the positions that the character has over 500ms periods. Each such period I call a Record. When 500ms has passed, the Record is sent to the server and a new Record starts recording. Therefore you see a 500ms lag when I move the character.