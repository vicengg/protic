import React from 'react';

export const Checkbox = ({value, setValue, labelOff, labelOn, type = "checkbox", disabled = false}) => {

    return (
        <div className={"custom-control custom-" + type} onClick={ setValue }>
            <input type="checkbox" className="custom-control-input" checked={ value } onChange={ setValue } disabled={disabled}/>
            <label className="custom-control-label">{ !!value ? labelOn : labelOff }</label>
        </div>
    );
}