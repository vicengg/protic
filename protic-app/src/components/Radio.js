import React from 'react';

export const Radio = ({ options, selected, changeOption }) => {

    const handleChange = (event) => {
        changeOption(event.target.value);
    }

    return (
        <>
            {!!options && Object.keys(options).map((key) => {
                return <div className="form-check" key={key}>
                    <input className="form-check-input" type="radio" value={key} onChange={handleChange} checked={options[key] === selected} />
                    <label className="form-check-label">{options[key]}</label>
                </div>
            })}
        </>
    );
}