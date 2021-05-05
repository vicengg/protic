import React from 'react';
import { Chip } from './Chip';

export const WorkExperience = ({ workExperience }) => {

    return (
        <div className="card">
            <div className="card-body">
                {!!workExperience.jobTitle && <h5 className="card-title">
                    {workExperience.jobTitle.content}
                    {!!workExperience.company && ` (${workExperience.company.content})`}
                </h5>}
                {!workExperience.jobTitle && !!workExperience.company && <h5 className="card-title">
                    {workExperience.company.content}
                </h5>}
                {!workExperience.jobTitle && !workExperience.company && 
                <h5 className="card-title">Experiencia laboral <ion-icon name="lock-closed"></ion-icon></h5>}
                <div className="d-flex flex-row">
                    <div>
                        <strong>Cargo: </strong>
                        {!!workExperience.jobTitle && <span>{workExperience.jobTitle.content}</span>}
                        {!workExperience.jobTitle && <span className="">Oculto <ion-icon name="lock-closed"></ion-icon></span>}
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Empresa: </strong>
                        {!!workExperience.company && <span>{workExperience.company.content}</span>}
                        {!workExperience.company && <span className="">Oculto <ion-icon name="lock-closed"></ion-icon></span>}
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Período de trabajo: </strong>
                        {!!workExperience.workPeriod && <span>
                            {`Desde ${workExperience.workPeriod.content.startDate} hasta 
                            ${!!workExperience.workPeriod.content.endDate ? workExperience.workPeriod.content.endDate : 'la actualidad'}`}
                        </span>}
                        {!workExperience.workPeriod && <span className="">Oculto <ion-icon name="lock-closed"></ion-icon></span>}
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Salario bruto anual: </strong>
                        {!!workExperience.salary && <span>{`${workExperience.salary.content.value} ${workExperience.salary.content.currency}`}</span>}
                        {!workExperience.salary && <span className="">Oculto <ion-icon name="lock-closed"></ion-icon></span>}
                    </div>
                </div>
                <div className="d-flex flex-row">
                    <div>
                        <strong>Tecnologías: </strong>
                        {!!workExperience.technologies && <div className="mt-1">
                             <div className="card-text">
                                {workExperience.technologies.content.map(technology => <Chip key={technology} value={technology} />)}
                            </div>
                        </div>}
                        {!workExperience.technologies && <span className="">Oculto <ion-icon name="lock-closed"></ion-icon></span>}
                    </div>
                </div>
            </div>
        </div>
    );
}