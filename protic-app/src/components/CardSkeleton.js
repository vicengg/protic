import React from 'react';

export const CardSkeleton = ({ lines = 5 }) => {

    const range = [...Array(lines).keys()];

    return (
        <>
            <div className="card">
                <div className="card-body">
                    <div className="card-title card-skeleton-title"></div>
                    {!!range && range.map(line => {
                        if(line % 2 === 0) {
                        return <div className="card-title card-skeleton-line-30" key={line}></div>
                        } else {
                            return <div className="card-title card-skeleton-line-40" key={line}></div>
                        }
                    })}
                </div>
            </div>
        </>
    );
}