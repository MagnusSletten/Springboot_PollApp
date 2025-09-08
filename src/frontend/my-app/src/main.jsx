import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './app.css';
import ReactDOM from "react-dom/client";
import React from "react";
import { RouterProvider } from "react-router-dom";
import { createBrowserRouter } from "react-router";
import Root from "./Components/Root";
import CreatePoll from "./Components/CreatePollComponent";



const router = createBrowserRouter([
  { 
path: "/", 
Component: Root ,
children: [
    {path: "new-poll", 
    Component: CreatePoll}

  ]
}
]);


ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
