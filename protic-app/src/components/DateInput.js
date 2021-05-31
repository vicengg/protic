import React from 'react';

export const DateInput = ({ footer, value, onChange, disabled = false, styleClasses = "" }) => {

    const handleInputChange = (event) => {
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
                            className={`form-control ${styleClasses}`}
                            type="date"
                            value={value}
                            onChange={handleInputChange}
                            readOnly={disabled}
                        />
                    </div>
                    {!!footer && <small className="form-text text-muted">{footer}</small>}
                </div>
            </form>
        </>
    );

};
