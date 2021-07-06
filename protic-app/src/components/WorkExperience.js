import React from 'react';
import { Chip } from './Chip';
import { Link } from "react-router-dom";

export const WorkExperience = ({ workExperience, enableNegotiation = true }) => {

    const isUnlocked = (field) => {
        return !!workExperience[field] && !workExperience[field].public;
    }

    return (
        <div className="card">
            <div className="card-body">
                <div className="row">
                    <div className="col-md-10">
                        {!!workExperience.jobTitle && <h5 className="card-title">
                            {workExperience.jobTitle.content}
                            {!!workExperience.company && ` (${workExperience.company.content})`}
                        </h5>}
                        {!workExperience.jobTitle && !!workExperience.company && <h5 className="card-title">
                            {workExperience.company.content}
                        </h5>}
                        {!workExperience.jobTitle && !workExperience.company &&
                            <h5 className="card-title">Experiencia laboral <ion-icon name="lock-closed-outline"></ion-icon></h5>}
                        <div className="d-flex flex-row">
                            <div className={isUnlocked("jobTitle") ? "text-success" : ""}>
                                <strong>Puesto de trabajo: </strong>
                                {!!workExperience.jobTitle && <span>{workExperience.jobTitle.content}</span>}
                                {!workExperience.jobTitle && <span>Oculto <ion-icon name="lock-closed-outline"></ion-icon></span>}
                                {isUnlocked("jobTitle") && <span> <ion-icon name="lock-open-outline"></ion-icon></span>}
                            </div>
                        </div>
                        <div className="d-flex flex-row">
                            <div className={isUnlocked("company") ? "text-success" : ""}>
                                <strong>Empresa: </strong>
                                {!!workExperience.company && <span>{workExperience.company.content}</span>}
                                {!workExperience.company && <span>Oculto <ion-icon name="lock-closed-outline"></ion-icon></span>}
                                {isUnlocked("company") && <span> <ion-icon name="lock-open-outline"></ion-icon></span>}
                            </div>
                        </div>
                        <div className="d-flex flex-row">
                            <div className={isUnlocked("workPeriod") ? "text-success" : ""}>
                                <strong>Período de trabajo: </strong>
                                {!!workExperience.workPeriod && <span>
                                    {`Desde ${workExperience.workPeriod.content.startDate} hasta 
                            ${!!workExperience.workPeriod.content.endDate ? workExperience.workPeriod.content.endDate : 'la actualidad'}`}
                                </span>}
                                {!workExperience.workPeriod && <span>Oculto <ion-icon name="lock-closed-outline"></ion-icon></span>}
                                {isUnlocked("workPeriod") && <span> <ion-icon name="lock-open-outline"></ion-icon></span>}
                            </div>
                        </div>
                        <div className="d-flex flex-row">
                            <div className={isUnlocked("salary") ? "text-success" : ""}>
                                <strong>Salario bruto anual: </strong>
                                {!!workExperience.salary && <span>{`${workExperience.salary.content.value} ${workExperience.salary.content.currency}`}</span>}
                                {!workExperience.salary && <span>Oculto <ion-icon name="lock-closed-outline"></ion-icon></span>}
                                {isUnlocked("salary") && <span> <ion-icon name="lock-open-outline"></ion-icon></span>}
                            </div>
                        </div>
                        <div className="d-flex flex-row">
                            <div className={isUnlocked("technologies") ? "text-success" : ""}>
                                <strong>Tecnologías: </strong>
                                {isUnlocked("technologies") && <span> <ion-icon name="lock-open-outline"></ion-icon></span>}
                                {!!workExperience.technologies && <div className="mt-1">
                                    <div className="card-text">
                                        {workExperience.technologies.content.map(technology => <Chip key={technology} value={technology} />)}
                                    </div>
                                </div>}
                                {!workExperience.technologies && <span>Oculto <ion-icon name="lock-closed-outline"></ion-icon></span>}
                            </div>
                        </div>
                    </div>
                    <div className="col-md-2">
                        <figure className="figure float-right">
                            <img width="100" height="100" src={workExperience.user.avatarUrl} className="rounded" alt={workExperience.user.login} />
                            <figcaption className="figure-caption text-center">
                                <h6>{workExperience.user.login}</h6>
                            </figcaption>
                        </figure>
                    </div>
                </div>
                {(!workExperience.jobTitle || !workExperience.company || !workExperience.technologies || !workExperience.workPeriod || !workExperience.salary) && enableNegotiation
                    && <div className="text-right">
                        <Link to={`/create-information-request/${workExperience.id}`}>
                            <button className="ml-1 btn btn-primary">Solicitar más información</button>
                        </Link>
                    </div>
                }
            </div>

        </div>
    );
}