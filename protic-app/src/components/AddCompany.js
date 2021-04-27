import React, { useState } from 'react';

export const AddCompany = ({ setCompanies }) => {

    const [inputValue, setInputValue] = useState("Hola mundo");

    const handleChange = (event) => {
        setInputValue(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        setCompanies(companies => [...companies, inputValue]);
        setInputValue('');
    };

    return  (
        <>
            <form onSubmit={ handleSubmit }>
                <input 
                type="text" 
                value={ inputValue } 
                onChange={ handleChange }/>
            </form>
        </>
    );

};
