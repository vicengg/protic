import React, { useState } from 'react';
import { Autocomplete } from './components/Autocomplete';
import { AutocompleteMultiple } from './components/AutocompleteMultiple';

import './index.css';

const ProticApp = () => {

    const [form, setForm] = useState({
        jobTitle: '',
        company: '',
        technologies: [],
    });

    const changeField = (field) => {
        return (value) => {
            setForm({...form, [field]: value});
        }
    }

    return  (
        <>
            <div className="container">
                <div className="row">   
                    <span>form.jobTitle: { form.jobTitle }</span>
                    <span>form.company: { form.company }</span>
                </div>
                <div className="row">   
                    <div className="col-md-4">
                    <Autocomplete 
                        url="/data/job-titles?name=" 
                        label="Profesión" 
                        placeholder="Introduce tu profesión" 
                        footer="Haz click para obtener sugerencias de profesiones."
                        value={ form.jobTitle }
                        onChange={ changeField('jobTitle') }
                        onSelect={ changeField('jobTitle') }
                        onSubmit={ changeField('jobTitle') }/>
                    </div>
                    <div className="col-md-4">
                    <Autocomplete 
                        url="/data/companies?name=" 
                        label="Empresa" 
                        placeholder="Introduzca una empresa" 
                        footer="Haz click para obtener sugerencias de empresas."
                        value={ form.company }
                        onChange={ changeField('company') }
                        onSelect={ changeField('company') }
                        onSubmit={ changeField('company') }/>
                    </div>
                    <div className="col-md-4">
                    <AutocompleteMultiple
                        url="/data/technologies?name=" 
                        label="Tecnologías" 
                        placeholder="Introduzca un nombre de tecnología" 
                        footer="Haz click para obtener sugerencias de tecnologías."
                        values={ form.technologies }
                        setValues={ changeField('technologies') }/>
                    </div>
                </div>
            </div>
        </>
    );

}

export default ProticApp;