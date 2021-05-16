import React from 'react';
import { Chip } from './Chip';
import { Checkbox } from './Checkbox';
import { RequestButton } from './RequestButton';
import { Link } from "react-router-dom";

export const OwnWorkExperience = ({ workExperience, afterDelete, editable = false }) => {

    return (
        <div className="card">
            <div className="card-body">
                {!!workExperience.jobTitle && <h5 className="card-title">
                    {workExperience.jobTitle.content}
                    {!!workExperience.company && ` (${workExperience.company.content})`}
                </h5>}
                <div className="d-flex flex-row">
                    <div>
                        <strong>Cargo: </strong>
                        <span>{workExperience.jobTitle.content}</span>
                    </div>
                    <div className="ml-auto">
                        <Checkbox value={workExperience.jobTitle.public} labelOff="Privado" labelOn="Público" type="switch" disabled={true} />
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Empresa: </strong>
                        <span>{workExperience.company.content}</span>
                    </div>
                    <div className="ml-auto">
                        <Checkbox value={workExperience.company.public} labelOff="Privado" labelOn="Público" type="switch" disabled={true} />
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Período de trabajo: </strong>
                        <span>
                            {`Desde ${workExperience.workPeriod.content.startDate} hasta 
                            ${!!workExperience.workPeriod.content.endDate ? workExperience.workPeriod.content.endDate : 'la actualidad'}`}
                        </span>
                    </div>
                    <div className="ml-auto">
                        <Checkbox value={workExperience.workPeriod.public} labelOff="Privado" labelOn="Público" type="switch" disabled={true} />
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Salario bruto anual: </strong>
                        <span>{workExperience.salary.content.value} {workExperience.salary.content.currency}</span>
                    </div>
                    <div className="ml-auto">
                        <Checkbox value={workExperience.salary.public} labelOff="Privado" labelOn="Público" type="switch" disabled={true} />
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Tecnologías: </strong>
                        <div className="mt-1">
                            {!!workExperience.technologies && <div className="card-text">
                                {workExperience.technologies.content.map(technology => <Chip key={technology} value={technology} />)}
                            </div>}
                        </div>
                    </div>
                    <div className="ml-auto">
                        <Checkbox value={workExperience.technologies.public} labelOff="Privado" labelOn="Público" type="switch" disabled={true} />
                    </div>
                </div>

            </div>
            {!!editable && <div className="card-body">
                <RequestButton
                    text="Eliminar"
                    url={`/work-experience/${workExperience.id}`}
                    method="DELETE"
                    styleClasses="btn-outline-primary"
                    onSuccess={afterDelete} />
                <Link to={`/modify-work-experience/${workExperience.id}`}>
                    <button className="ml-1 btn btn-outline-primary">Modificar</button>
                </Link>
            </div>}

        </div>
    );
}