import React from 'react';

export const ClosableChip = ({ value, onClose }) => {

    const applyClose = () => {
        onClose(value);
    }

    return (
        <>
            <div className="component-closable-chip">
                <span className="chip-text">{value}</span>
                <span className="close-btn" onClick={ applyClose }>&times;</span>
            </div>
        </>
    );
}