import React, { useState } from 'react';
import { Autocomplete } from '../Autocomplete';
import { AutocompleteMultiple } from '../AutocompleteMultiple';
import { DateInput } from '../DateInput';
import { MoneyInput } from '../MoneyInput';
import { Switch } from '../Switch';


export const AddWorkExperienceView = () => {

    const [form, setForm] = useState({
        jobTitle: {
            public: false,
            content: ''
        },
        company: {
            public: false,
            content: ''
        },
        technologies: {
            public: false,
            content: []
        },
        salary: {
            public: false,
            content: 0
        },
        workPeriod: {
            public: false,
            content: {
                startDate: '',
                endDate: null
            }
        }
    });


    const setStartDate = () => {
        return (value) => {
            setForm({ ...form, workPeriod: { ...form.workPeriod, content: { ...form.workPeriod.content, startDate: value } } });
        }
    }

    const changeField = (field) => {
        return (value) => {
            setForm({ ...form, [field]: { ...form[field], content: value } });
        }
    }

    const toggleVisibility = (field) => {
        return () => {
            setForm({ ...form, [field]: { ...form[field], public: !form[field].public } });
        }
    }

    return (
        <>
            <div className="container">
                <div className="row mb-5">
                    <div className="col-md-12">
                        <code>{JSON.stringify(form)}</code>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-4">
                        <label>Profesión</label>
                        <Autocomplete
                            url="/data/job-titles?name="
                            placeholder="Introduce tu profesión"
                            footer="Haz click para obtener sugerencias de profesiones."
                            value={form.jobTitle.content}
                            onChange={changeField('jobTitle')}
                            onSelect={changeField('jobTitle')}
                            onSubmit={changeField('jobTitle')} />
                        <Switch
                            value={form.jobTitle.public}
                            setValue={toggleVisibility('jobTitle')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                    <div className="col-md-4">
                        <label>Empresa</label>
                        <Autocomplete
                            url="/data/companies?name="
                            placeholder="Introduzca una empresa"
                            footer="Haz click para obtener sugerencias de empresas."
                            value={form.company.content}
                            onChange={changeField('company')}
                            onSelect={changeField('company')}
                            onSubmit={changeField('company')} />
                        <Switch
                            value={form.company.public}
                            setValue={toggleVisibility('company')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                    <div className="col-md-4">
                        <label>Tecnologías</label>
                        <AutocompleteMultiple
                            url="/data/technologies?name="
                            placeholder="Introduzca un nombre de tecnología"
                            footer="Haz click para obtener sugerencias de tecnologías."
                            values={form.technologies.content}
                            setValues={changeField('technologies')} />
                        <Switch
                            value={form.technologies.public}
                            setValue={toggleVisibility('technologies')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                    <div className="col-md-4">
                        <label>Salario bruto anual</label>
                        <MoneyInput
                            placeholder="Introduzca su salario"
                            footer="Salario bruto anual en Euros."
                            value={form.salary.content}
                            onChange={changeField('salary')} />
                        <Switch
                            value={form.salary.public}
                            setValue={toggleVisibility('salary')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                    <div className="col-md-4">
                        <label>Salario bruto anual</label>
                        <MoneyInput
                            placeholder="Introduzca su salario"
                            footer="Salario bruto anual en Euros."
                            value={form.salary.content}
                            onChange={changeField('salary')} />
                        <Switch
                            value={form.salary.public}
                            setValue={toggleVisibility('salary')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                    <div className="col-md-4">
                        <label>Fecha de inicio</label>
                        <DateInput
                            placeholder="Introduzca fecha de inicio"
                            footer="Fecha de inicio."
                            value={form.workPeriod.content.startDate}
                            onChange={setStartDate} />
                        <Switch
                            value={form.workPeriod.public}
                            setValue={toggleVisibility('workPeriod')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                </div>
            </div>
        </>
    );

};
