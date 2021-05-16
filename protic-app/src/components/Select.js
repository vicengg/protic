import React from 'react';

export const Select = ({ values, onChange}) => {

    const handleChange = (event) => {
        onChange(event.target.value);
    };

    const handleFormSubmit = (event) => {
        event.preventDefault();
    };

    return (
        <>
            <form onSubmit={handleFormSubmit}>
                <select className="form-control" onChange={ handleChange }>
                    {!!values && values.map(value => <option key={value.key} value={value.key}>{value.value}</option>)}
                </select>
            </form>
        </>
    );

};
