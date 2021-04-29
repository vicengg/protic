import React, { useState } from 'react';
import { Autocomplete } from '../Autocomplete';
import { AutocompleteMultiple } from '../AutocompleteMultiple';
import { DateInput } from '../DateInput';
import { MoneyInput } from '../MoneyInput';
import { Checkbox } from '../Checkbox';
import { emptyIfNull } from '../../helpers/nullHelpers';
import { useWorkExperience } from '../../hooks/useWorkExperience';
import { Radio } from '../Radio';


export const AddWorkExperienceView = () => {

    const [form, changeField, toggleVisibility, changeWorkPeriodDate] = useWorkExperience();

    const workPeriodOptions = {
        "until_now": "Hasta la actualidad",
        "select_date": "Hasta..."
    };

    const [workPeriodSelectedOption, setWorkPeriodSelectedOption] = useState(workPeriodOptions["until_now"]);

    const changeWorkPeriodOption = (key) => {
        setWorkPeriodSelectedOption(workPeriodOptions[key]);
        if(key === "until_now") {
            changeWorkPeriodDate("endDate")(null);
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
                            value={emptyIfNull(form.jobTitle.content)}
                            onChange={changeField('jobTitle')}
                            onSelect={changeField('jobTitle')}
                            onSubmit={changeField('jobTitle')} />
                        <Checkbox
                            type="switch"
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
                            value={emptyIfNull(form.company.content)}
                            onChange={changeField('company')}
                            onSelect={changeField('company')}
                            onSubmit={changeField('company')} />
                        <Checkbox
                            type="switch"
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
                        <Checkbox
                            type="switch"
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
                        <Checkbox
                            type="switch"
                            value={form.salary.public}
                            setValue={toggleVisibility('salary')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                    <div className="col-md-4">
                        <label>Fecha de inicio</label>
                        <DateInput
                            type="switch"
                            footer="Fecha de inicio."
                            value={emptyIfNull(form.workPeriod.content.startDate)}
                            onChange={changeWorkPeriodDate("startDate")} />
                        <Radio options={workPeriodOptions} selected={workPeriodSelectedOption} changeOption={changeWorkPeriodOption} />
                        <DateInput
                            type="switch"
                            footer="Fecha de inicio."
                            value={emptyIfNull(form.workPeriod.content.endDate)}
                            onChange={changeWorkPeriodDate("endDate")}
                            disabled={workPeriodSelectedOption !== workPeriodOptions["select_date"]} />
                        <Checkbox
                            type="switch"
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
