import React from 'react';

export const RequestButton = ({ text, url, method = 'GET', body = '', headers = { 'Content-Type': 'application/json' }, styleClasses = "btn-primary", onSuccess }) => {

    const handleClick = () => {
        const request = new Request(url, { method, headers, body: ['GET', 'DELETE'].includes(method) ? null : JSON.stringify(body) });
        fetch(request)
            .then(response => {
                !!onSuccess && onSuccess(response);
            });
    }

    return (
        <button type="button" className={`btn ${styleClasses}`} onClick={handleClick}>{text}</button>
    );
}