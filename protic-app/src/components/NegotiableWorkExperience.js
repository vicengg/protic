import React, { useEffect } from 'react';

export const NegotiableWorkExperience = ({ workExperience, children, visibilityRequest, setVisibilityRequest, disabled = false }) => {

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

    const isChecked = (field) => {
        return !!visibilityRequest && (visibilityRequest[field].toLowerCase() === "already_visible" || visibilityRequest[field].toLowerCase() === "make_public");
    }

    const isAlreadyVisible = (field) => {
        return !!visibilityRequest && (visibilityRequest[field].toLowerCase() === "already_visible");
    }

    const toggle = (field) => {
        if (!disabled) {
            return () => {
                if (visibilityRequest[field].toLowerCase() === "make_public") {
                    setVisibilityRequest({ ...visibilityRequest, [field]: "keep_private" })
                } else if (visibilityRequest[field].toLowerCase() === "keep_private") {
                    setVisibilityRequest({ ...visibilityRequest, [field]: "make_public" })
                }
            }
        }
    }

    return (
        <div className="card">
            <div className="card-body">
                <div className="row">
                    <div className="col-md-8">
                        {children[0]}
                        {children[1]}
                    </div>
                    <div className="col-md-4">
                        {!!workExperience && !!workExperience.user && <figure className="figure float-right">
                            <img width="100" height="100" src={workExperience.user.avatarUrl} className="rounded" alt={workExperience.user.login} />
                            <figcaption className="figure-caption text-center">
                                <h6>{workExperience.user.login}</h6>
                            </figcaption>
                        </figure>}
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-12">
                        {children[2]}
                    </div>
                </div>
            </div>
            <ul className="list-group  list-group-flush">
                <li className="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>Puesto de trabajo: </strong>
                        {isPresent(workExperience.jobTitle) && <span>{workExperience.jobTitle.content} </span>}
                        {!isPublic(workExperience.jobTitle) && <ion-icon name="lock-closed"></ion-icon>}
                    </div>
                    <div>
                        {!isAlreadyVisible("jobTitle") && <div className="custom-control custom-switch" onClick={toggle("jobTitle")}>
                            <input
                                type="checkbox"
                                className="custom-control-input"
                                checked={isChecked("jobTitle")}
                                onChange={toggle("jobTitle")}
                                disabled={disabled} />
                            <label className={`custom-control-label h5 ${isChecked("jobTitle") ? "text-primary" : "text-muted"}`}>
                                {!isChecked("jobTitle") && <ion-icon name="lock-closed-outline"></ion-icon>}
                                {isChecked("jobTitle") && <ion-icon name="lock-open-outline"></ion-icon>}
                            </label>
                        </div>
                        }
                    </div>
                </li>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>Empresa: </strong>
                        {isPresent(workExperience.company) && <span>{workExperience.company.content} </span>}
                        {!isPublic(workExperience.company) && <ion-icon name="lock-closed"></ion-icon>}
                    </div>
                    <div>
                        {!isAlreadyVisible("company") &&
                            <div className="custom-control custom-switch" onClick={toggle("company")}>
                                <input
                                    type="checkbox"
                                    className="custom-control-input"
                                    checked={isChecked("company")}
                                    onChange={toggle("company")}
                                    disabled={disabled} />
                                <label className={`custom-control-label h5 ${isChecked("company") ? "text-primary" : "text-muted"}`}>
                                    {!isChecked("company") && <ion-icon name="lock-closed-outline"></ion-icon>}
                                    {isChecked("company") && <ion-icon name="lock-open-outline"></ion-icon>}
                                </label>
                            </div>
                        }
                    </div>
                </li>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>Tecnologías: </strong>
                        {isPresent(workExperience.technologies) && <span>{workExperience.technologies.content.join(', ')} </span>}
                        {!isPublic(workExperience.technologies) && <ion-icon name="lock-closed"></ion-icon>}
                    </div>
                    <div>
                        {!isAlreadyVisible("technologies") &&
                            <div className="custom-control custom-switch" onClick={toggle("technologies")}>
                                <input
                                    type="checkbox"
                                    className="custom-control-input"
                                    checked={isChecked("technologies")}
                                    onChange={toggle("technologies")}
                                    disabled={disabled} />
                                <label className={`custom-control-label h5 ${isChecked("technologies") ? "text-primary" : "text-muted"}`}>
                                    {!isChecked("technologies") && <ion-icon name="lock-closed-outline"></ion-icon>}
                                    {isChecked("technologies") && <ion-icon name="lock-open-outline"></ion-icon>}
                                </label>
                            </div>
                        }
                    </div>
                </li>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>Salario: </strong>
                        {isPresent(workExperience.salary) && <span>{workExperience.salary.content.value} {workExperience.salary.content.currency} </span>}
                        {!isPublic(workExperience.salary) && <ion-icon name="lock-closed"></ion-icon>}
                    </div>
                    <div>
                        {!isAlreadyVisible("salary") &&
                            <div className="custom-control custom-switch" onClick={toggle("salary")}>
                                <input
                                    type="checkbox"
                                    className="custom-control-input"
                                    checked={isChecked("salary")}
                                    onChange={toggle("salary")}
                                    disabled={disabled} />
                                <label className={`custom-control-label h5 ${isChecked("salary") ? "text-primary" : "text-muted"}`}>
                                    {!isChecked("salary") && <ion-icon name="lock-closed-outline"></ion-icon>}
                                    {isChecked("salary") && <ion-icon name="lock-open-outline"></ion-icon>}
                                </label>
                            </div>
                        }
                    </div>
                </li>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>Período: </strong>
                        {isPresent(workExperience.workPeriod) && <span>{workExperience.workPeriod.content.startDate} </span>}
                        {isPresent(workExperience.workPeriod) && "-"}
                        {isPresent(workExperience.workPeriod) && isPresent(workExperience.workPeriod.content.endDate) && <span> {workExperience.workPeriod.content.endDate} </span>}
                        {isPresent(workExperience.workPeriod) && !isPresent(workExperience.workPeriod.content.endDate) && <span> Actualidad </span>}
                        {!isPublic(workExperience.workPeriod) && <ion-icon name="lock-closed"></ion-icon>}
                    </div>
                    <div>
                        {!isAlreadyVisible("workPeriod") &&
                            <div className="custom-control custom-switch" onClick={toggle("workPeriod")}>
                                <input
                                    type="checkbox"
                                    className="custom-control-input"
                                    checked={isChecked("workPeriod")}
                                    onChange={toggle("workPeriod")}
                                    disabled={disabled} />
                                <label className={`custom-control-label h5 ${isChecked("workPeriod") ? "text-primary" : "text-muted"}`}>
                                    {!isChecked("workPeriod") && <ion-icon name="lock-closed-outline"></ion-icon>}
                                    {isChecked("workPeriod") && <ion-icon name="lock-open-outline"></ion-icon>}
                                </label>
                            </div>
                        }
                    </div>
                </li>
            </ul>
        </div>
    );
}