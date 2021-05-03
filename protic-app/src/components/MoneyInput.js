import React from 'react';

export const MoneyInput = ({ placeholder, footer, value, onChange, currency = "EUR" }) => {


    const handleInputChange = (event) => {
        onChange({ value: event.target.value, currency });
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
                            type="number"
                            min="0"
                            step="1000"
                            value={value.value}
                            onChange={handleInputChange}
                            placeholder={placeholder}
                        />
                        <div className="input-group-append">
                            <span className="input-group-text">€</span>
                        </div>
                    </div>
                    {!!footer && <small className="form-text text-muted">{footer}</small>}
                </div>
            </form>
        </>
    );

};
