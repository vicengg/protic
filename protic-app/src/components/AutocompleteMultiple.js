import React, { useState } from 'react';
import { Autocomplete } from './Autocomplete';
import { ClosableChip } from './ClosableChip';

export const AutocompleteMultiple = ({ url, placeholder, footer, values, setValues }) => {

    const [temporalValue, setTemporalValue] = useState('');
    const addValue = (newValue) => {
        setValues(Array.from(new Set([...values, newValue])));
        setTemporalValue('');
    }

    const removeFromValues = (valueToRemove) => {
        setValues(values.filter(element => element !== valueToRemove));
    }

    return (
        <>
            <Autocomplete
                url={url}
                placeholder={placeholder}
                footer={footer}
                value={temporalValue}
                onChange={setTemporalValue}
                onSelect={addValue}
                onSubmit={addValue} />
            {!!values && values.map(value => {
                return <ClosableChip value={value} key={value} onClose={removeFromValues} />
            })}
        </>
    );

};
