import React, { useState, useEffect, useRef } from 'react';
import { Autocomplete } from './Autocomplete';

export const AutocompleteMultiple = ({ url, placeholder, label, footer, values, setValues }) => {

    const [temporalValue, setTemporalValue] = useState('');

    const addValue = (newValue) => {
        setValues([...values, newValue])
        setTemporalValue('');
    }

    return (
        <>
            <Autocomplete 
                url={ url }
                label={ label }
                placeholder={ placeholder }
                footer={ footer }
                value={ temporalValue }
                onChange={ setTemporalValue }
                onSelect={ addValue }
                onSubmit={ addValue }/>
            {!!values && values.map(value => {
                return <span className="badge badge-primary badge-pill" key={ value }>{ value }</span>;
            })}
        </>
    );

};
