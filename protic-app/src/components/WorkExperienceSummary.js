import React, { useEffect } from 'react';

export const WorkExperienceSummary = ({ workExperience, children, visibilityRequest, setVisibilityRequest, disabled = false }) => {

    const isPresent = (field) => {
        return !!field;
    }

    const isPublic = (field) => {
        return isPresent(field) && !!field.public;
    }

    useEffect(() => {
        if (!visibilityRequest) {
            if (!!workExperience) {
                setVisibilityRequest({
                    jobTitle: isPublic(workExperience.jobTitle) ? "already_visible" : "keep_private",
                    company: isPublic(workExperience.company) ? "already_visible" : "keep_private",
                    technologies: isPublic(workExperience.technologies) ? "already_visible" : "keep_private",
                    workPeriod: isPublic(workExperience.workPeriod) ? "already_visible" : "keep_private",
                    salary: isPublic(workExperience.salary) ? "already_visible" : "keep_private"
                })
            }
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [workExperience, visibilityRequest]);

    const workExperienceTitle = (workExperience) => {
        if (!!workExperience && !!workExperience.jobTitle) {
            if (!!workExperience.company) {
                return `${workExperience.jobTitle.content} (${workExperience.company.content})`;
            } else {
                return `${workExperience.jobTitle.content}`
            }
        } else {
            if (!!workExperience && !!workExperience.company) {
                return `${workExperience.company.content}`;
            } else {
                return "Experiencia laboral"
            }
        }
    }

    return (
        <div className="card">
            <div className="card-body">
                <div className="row">
                    <div className="col-md-12">
                        <div className="row">
                            <div className="col-md-3">
                                {!!workExperience && !!workExperience.user &&
                                    <figure className="figure float-left">
                                        <img width="50" height="50" src={workExperience.user.avatarUrl} className="rounded" alt={workExperience.user.login} />
                                        <figcaption className="figure-caption text-center">
                                            <small>{workExperience.user.login}</small>
                                        </figcaption>
                                    </figure>
                                }
                            </div>
                            <div className="col-md-9">
                                <div className="lead">{workExperienceTitle(workExperience)}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-12">
                        <div>
                            <small>
                                <strong>Cargo: </strong>
                                {isPresent(workExperience.jobTitle) && <span>{workExperience.jobTitle.content} </span>}
                                {!isPublic(workExperience.jobTitle) && <ion-icon name="lock-closed"></ion-icon>}
                            </small>
                        </div>
                        <div>
                            <small>
                                <strong>Empresa: </strong>
                                {isPresent(workExperience.company) && <span>{workExperience.company.content} </span>}
                                {!isPublic(workExperience.company) && <ion-icon name="lock-closed"></ion-icon>}
                            </small>
                        </div>
                        <div>
                            <small>
                                <strong>Tecnologías: </strong>
                                {isPresent(workExperience.technologies) && <span>{workExperience.technologies.content.join(', ')} </span>}
                                {!isPublic(workExperience.technologies) && <ion-icon name="lock-closed"></ion-icon>}
                            </small>
                        </div>
                        <div>
                            <small>
                                <strong>Salario: </strong>
                                {isPresent(workExperience.salary) && <span>{workExperience.salary.content.value} {workExperience.salary.content.currency} </span>}
                                {!isPublic(workExperience.salary) && <ion-icon name="lock-closed"></ion-icon>}
                            </small>
                        </div>
                        <div>
                            <small>
                                <strong>Período: </strong>
                                {isPresent(workExperience.workPeriod) && <span>{workExperience.workPeriod.content.startDate} </span>}
                                {isPresent(workExperience.workPeriod) && "-"}
                                {isPresent(workExperience.workPeriod) && isPresent(workExperience.workPeriod.content.endDate) && <span> {workExperience.workPeriod.content.endDate} </span>}
                                {isPresent(workExperience.workPeriod) && !isPresent(workExperience.workPeriod.content.endDate) && <span> Actualidad </span>}
                                {!isPublic(workExperience.workPeriod) && <ion-icon name="lock-closed"></ion-icon>}
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}