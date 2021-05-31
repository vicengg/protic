import React, { useState } from 'react';
import { Autocomplete } from '../Autocomplete';
import { AutocompleteMultiple } from '../AutocompleteMultiple';
import { DateInput } from '../DateInput';
import { MoneyInput } from '../MoneyInput';
import { Checkbox } from '../Checkbox';
import { emptyIfNull } from '../../helpers/nullHelpers';
import { useWorkExperience } from '../../hooks/useWorkExperience';
import { Radio } from '../Radio';
import { RequestButton } from '../RequestButton';
import { useHistory } from "react-router-dom";
import { useUser } from '../../hooks/useUser';


export const AddWorkExperienceView = () => {

    const { data: user } = useUser();

    const {workExperience: form, changeField, toggleVisibility, changeWorkPeriodDate, toggleBinding} = useWorkExperience();

    const workPeriodOptions = {
        "until_now": "Hasta la actualidad",
        "select_date": "Hasta..."
    };

    const [workPeriodSelectedOption, setWorkPeriodSelectedOption] = useState(workPeriodOptions["until_now"]);

    const changeWorkPeriodOption = (key) => {
        setWorkPeriodSelectedOption(workPeriodOptions[key]);
        if (key === "until_now") {
            changeWorkPeriodDate("endDate")(null);
        }
    }

    const history = useHistory();

    function redirectTo(path) {
        return () => history.push(path);
    }

    return (
        <>

            <div className="container">
                <h1>Añadir experiencia laboral</h1>
                <p className="text-justify">
                    Este panel te permite añadir una experiencia laboral.
                    Recuerda que tú eliges quién puede ver tus datos: utiliza el <strong>interruptor a la derecha</strong> de cada campo para elegir
                    si es visible para todo el mundo (<span className="text-primary">público</span>)
                    o por el contrario solo es visible para ti y para aquellos usuarios a los que ofrezcas voluntariamente tu información (<span className="text-primary">privado</span>).
                </p>
                <p className="text-justify">
                    Protic te permitirá <span className="text-primary">solicitar</span> datos privados de otros usuarios y <span className="text-primary">ofrecer</span> los tuyos propios a fin de conseguir información laboral de otras personas,
                    siempre por acuerdo mutuo y con el <span className="text-primary">nivel de anonimato</span> que elijas para tu experiencia laboral.
                </p>
                <h2>Información laboral</h2>
                <h6>Profesión</h6>
                <div className="row">
                    <div className="col-md-10">
                        <Autocomplete
                            url="/data/job-titles?name="
                            placeholder="Introduce tu profesión"
                            footer="Haz click para obtener sugerencias de profesiones. Puedes introducir una profesión que no esté en la lista  de sugerencias simplemente introduciendo su nombre."
                            value={emptyIfNull(form.jobTitle.content)}
                            onChange={changeField('jobTitle')}
                            onSelect={changeField('jobTitle')}
                            onSubmit={changeField('jobTitle')} />
                    </div>
                    <div className="col-md-2">
                        <Checkbox
                            type="switch"
                            value={form.jobTitle.public}
                            setValue={toggleVisibility('jobTitle')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                </div>
                <h6>Empresa</h6>
                <div className="row">
                    <div className="col-md-10">
                        <Autocomplete
                            url="/data/companies?name="
                            placeholder="Introduzca una empresa"
                            footer="Haz click para obtener sugerencias de expresas. Puedes introducir una empresa que no esté en la lista de sugerencias simplemente introduciendo su nombre."
                            value={emptyIfNull(form.company.content)}
                            onChange={changeField('company')}
                            onSelect={changeField('company')}
                            onSubmit={changeField('company')} />
                    </div>
                    <div className="col-md-2">
                        <Checkbox
                            type="switch"
                            value={form.company.public}
                            setValue={toggleVisibility('company')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                </div>
                <h6>Tecnologías</h6>
                <div className="row mb-3">
                    <div className="col-md-10">
                        <AutocompleteMultiple
                            url="/data/technologies?name="
                            placeholder="Introduzca un nombre de tecnología"
                            footer='Haz click para obtener sugerencias de tecnologías. Puedes introducir múltiples tecnologías relacionadas con tu experiencia laboral.
                            Para introducir una tecnología no sugerida, simplemente escribe su nombre y presiona la tecla "enter".'
                            values={form.technologies.content}
                            setValues={changeField('technologies')} />
                    </div>
                    <div className="col-md-2">
                        <Checkbox
                            type="switch"
                            value={form.technologies.public}
                            setValue={toggleVisibility('technologies')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                </div>
                <h6>Salario</h6>
                <div className="row">
                    <div className="col-md-10">
                        <MoneyInput
                            placeholder="Introduzca su salario"
                            footer="Introduce tu salario bruto anual en Euros."
                            value={form.salary.content}
                            onChange={changeField('salary')} />
                    </div>
                    <div className="col-md-2">
                        <Checkbox
                            type="switch"
                            value={form.salary.public}
                            setValue={toggleVisibility('salary')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                </div>
                <h6>Periodo laboral</h6>
                <div className="row">
                    <div className="col-md-4">
                        <DateInput
                            footer="Introduce la fecha de inicio."
                            value={emptyIfNull(form.workPeriod.content.startDate)}
                            onChange={changeWorkPeriodDate("startDate")} />
                    </div>
                    <div className="col-md-2">
                        <Radio options={workPeriodOptions} selected={workPeriodSelectedOption} changeOption={changeWorkPeriodOption} />
                    </div>
                    <div className="col-md-4">
                        <DateInput
                            footer="Introduce la fecha de fin (si procede)."
                            value={emptyIfNull(form.workPeriod.content.endDate)}
                            onChange={changeWorkPeriodDate("endDate")}
                            disabled={workPeriodSelectedOption !== workPeriodOptions["select_date"]} />
                    </div>
                    <div className="col-md-2">
                        <Checkbox
                            type="switch"
                            value={form.workPeriod.public}
                            setValue={toggleVisibility('workPeriod')}
                            labelOn="Público"
                            labelOff="Privado" />
                    </div>
                </div>
                <h2>Vincular experiencia con tu perfil de usuario</h2>
                <div className="row">
                    <div className="col-md-8">
                        <p className="text-justify">
                            Vincular una experiencia con tu perfil permitirá al resto de usuarios conocer a quién pertenece esta experiencia,
                            la verán asociada a tu nombre de usuario en el buscador de experiencias y en sus solicitudes de información.
                        </p>
                        <p className="text-justify">
                            No vincular esta experiencia a tu perfil te permitirá publicarla de forma anónima, de manera que nadie podrá saber a quién pertenece.
                        </p>
                    </div>
                    <div className="col-md-4 text-left">
                        <h6>Vincular experiencia con perfil de usuario</h6>
                        <div>
                            <Checkbox
                                value={form.binding}
                                setValue={toggleBinding}
                                labelOn="Vinculada a tu perfil"
                                labelOff="No vinculada a tu perfil" />
                        </div>
                        {!!form && !form.binding && !!user &&
                            <div className="pt-3">
                                <img width="60" height="60" src={user.avatarUrl} className="rounded float-left" alt={user.login} />
                                <div style={{ width: 60 }} className="p-1 ml-2 mr-2 text-center float-left">
                                    <div>
                                        <small>Anónimo</small>
                                    </div>
                                    <ion-icon name="arrow-forward" size="large"></ion-icon>
                                </div>
                                <img width="60" height="60" src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png" className="rounded float-left"
                                    alt="anónimo" />
                            </div>
                        }
                        {!!form && !!form.binding && !!user &&
                            <div className="pt-3">
                                <img width="60" height="60" src={user.avatarUrl} className="rounded float-left" alt={user.login} />
                                <div style={{ width: 60 }} className="p-1 ml-2 mr-2 text-center float-left">
                                    <div>
                                        <small>Visible</small>
                                    </div>
                                    <ion-icon name="arrow-forward" size="large"></ion-icon>
                                </div>
                                <img width="60" height="60" src={user.avatarUrl} className="rounded float-left" alt={user.login} />
                            </div>
                        }
                    </div>

                </div>
                <div className="row">
                    <div className="col-md-12 text-right pb-5">
                        <button className="btn btn-outline-secondary mr-1" onClick={redirectTo("/my-work-experiences")}>Cancelar</button>
                        <RequestButton
                            text="Crear experiencia laboral"
                            url="/work-experience"
                            method="POST"
                            body={form}
                            onSuccess={redirectTo("/my-work-experiences")} />
                    </div>
                </div>
            </div>

        </>
    );

};
