import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {ConfigProvider} from 'antd';

const darkTheme = {
    token: {
        colorBgBase: '#141414',
        colorTextBase: '#ffffff',
        colorPrimary: '#1890ff',
        colorLink: '#1890ff',
        colorBorder: '#444',
    },
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <ConfigProvider theme={darkTheme}>
            <App/>
        </ConfigProvider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();