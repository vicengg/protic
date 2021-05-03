import React from 'react';

export const Chip = ({ value }) => {

    return (
        <>
            <div className="component-closable-chip">
                <span className="chip-text">{value}</span>
            </div>
        </>
    );
}