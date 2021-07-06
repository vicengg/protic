import React from 'react';
import { formatDateTimeAgo } from '../helpers/dateHelpers';

export const ActionDisplay = ({ action, user, isFirst, creator, receiver }) => {

    const issuerIsCreator = () => {
        return action.issuer.id === creator.id;
    }

    const issuerIsUser = () => {
        return action.issuer.id === user.id;
    }

    const getAction = () => {
        if (isFirst) {
            return <span><strong>creó</strong> la solicitud</span>
        } else {
            if (isModification()) {
                return <span><strong>negoció</strong> la solicitud</span>
            } else if (isCancelation()) {
                return <span><strong>canceló</strong> la solicitud</span>
            } else if (isAcceptation()) {
                return <span><strong>aceptó</strong> la solicitud</span>
            }
        }
    }

    const isModification = () => {
        return action.type.toLowerCase() === "modify";
    }

    const isCancelation = () => {
        return action.type.toLowerCase() === "cancel";
    }

    const isAcceptation = () => {
        return action.type.toLowerCase() === "accept";
    }

    return (
        <>
            <div className={`${!issuerIsCreator() ? "col-md-8 offset-md-4" : "col-md-8"}`}>
                <div className={`card mt-3 ${issuerIsUser() ? "bg-light" : ""} ${isCancelation() ? "border-danger" : ""} ${isAcceptation() ? "border-success" : ""}`}>
                    <div className="card-body p-3">
                        <div className="d-flex justify-content-start">
                            <img width="30" height="30" src={action.issuer.avatarUrl} className="rounded mr-2" alt={action.issuer.login} />
                            <div>
                                {action.issuer.login} {getAction()} {formatDateTimeAgo(new Date(action.date))}.
                        </div>
                        </div>
                        {isModification() &&
                            <div className="row">
                                <div className="col-md-5 offset-md-1">
                                    <strong>{creator.login}</strong> mostrará:
                                    <ul className="mb-0">
                                        {action.offeredData.jobTitle.toLowerCase() === "make_public" && <li>Puesto de trabajo</li>}
                                        {action.offeredData.company.toLowerCase() === "make_public" && <li>Empresa</li>}
                                        {action.offeredData.technologies.toLowerCase() === "make_public" && <li>Tecnologías</li>}
                                        {action.offeredData.salary.toLowerCase() === "make_public" && <li>Salario</li>}
                                        {action.offeredData.workPeriod.toLowerCase() === "make_public" && <li>Periodo laboral</li>}
                                    </ul>
                                </div>
                                <div className="col-md-5 offset-md-1">
                                    <strong>{receiver.login}</strong> mostrará:
                                    <ul className="mb-0">
                                        {action.demandedData.jobTitle.toLowerCase() === "make_public" && <li>Puesto de trabajo</li>}
                                        {action.demandedData.company.toLowerCase() === "make_public" && <li>Empresa</li>}
                                        {action.demandedData.technologies.toLowerCase() === "make_public" && <li>Tecnologías</li>}
                                        {action.demandedData.salary.toLowerCase() === "make_public" && <li>Salario</li>}
                                        {action.demandedData.workPeriod.toLowerCase() === "make_public" && <li>Periodo laboral</li>}
                                    </ul>
                                </div>
                            </div>
                        }
                    </div>
                </div>
            </div>
        </>
    );
}