import React from 'react';

export const DateInput = ({ placeholder, footer, value, onChange }) => {


    const handleInputChange = (event) => {
        console.log(event.target.value);
        onChange(event.target.value);
    };

    const handleFormSubmit = (event) => {
        event.preventDefault();
    };

    return (
        <>
            <form onSubmit={handleFormSubmit} autoComplete="off" className="component-autocomplete">
                <div className="input-container form-group">
                    <div className="input-group">
                        <input
                            className="form-control"
                            placeholder={placeholder}
                            type="date"
                            value={value}
                            onChange={handleInputChange}
                        />
                    </div>
                    {!!footer && <small className="form-text text-muted">{footer}</small>}
                </div>
            </form>
        </>
    );

};
