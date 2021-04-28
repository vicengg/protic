import React from 'react';

export const Switch = ({value, setValue, labelOff, labelOn}) => {

    return (
        <div className="custom-control custom-switch" onClick={ setValue }>
            <input type="checkbox" className="custom-control-input" checked={ value } onChange={ setValue }/>
            <label className="custom-control-label">{ !!value ? labelOn : labelOff }</label>
        </div>
    );
}