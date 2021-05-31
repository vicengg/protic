import React from 'react';
import { formatDateTimeAgo } from '../helpers/dateHelpers';
import { cancel as actionCancel, accept as actionAccept } from '../helpers/negotationActions';
import { useHistory } from "react-router-dom";

export const NegotiationSummary = ({ negotiation, user, afterOperate }) => {

    const history = useHistory();

    const isUser = (otherUser) => {
        return user.id === otherUser.id;
    }

    const getLastAction = () => {
        return !!negotiation.actions && !!negotiation.actions.length &&
            negotiation.actions[negotiation.actions.length - 1];
    }

    const isPending = () => {
        return getLastAction().type.toLowerCase() === "modify";
    }

    const isAccepted = () => {
        return getLastAction().type.toLowerCase() === "accept";
    }

    const isCancelled = () => {
        return getLastAction().type.toLowerCase() === "cancel";
    }

    const getTheOtherUser = () => {
        if (isUser(negotiation.creator)) {
            return negotiation.receiver;
        } else {
            return negotiation.creator;
        }
    }

    const getNegotiationStatus = () => {
        if (isAccepted()) {
            return (
                <div className="text-center text-success">
                    <span className="h1"><ion-icon name="checkmark-circle-outline"></ion-icon></span><br />
                    <small>Aceptada</small>
                </div>
            )
        }
        if (isCancelled()) {
            return (
                <div className="text-center text-danger">
                    <span className="h1"><ion-icon name="close-circle-outline"></ion-icon></span><br />
                    <small>Cancelada</small>
                </div>
            )
        }
        if (isUser(negotiation.nextActor)) {
            return (
                <div className="text-center text-primary">
                    <span className="h1"><ion-icon name="send-outline"></ion-icon></span><br />
                    <small>Pendiente de tu respuesta</small>
                </div>
            )
        } else {
            return (
                <div className="text-center text-info">
                    <span className="h1"><ion-icon name="time-outline"></ion-icon></span><br />
                    <small>{`Esperando respuesta (de ${negotiation.nextActor.login})`}</small>
                </div>
            )
        }
    }

    const getOfferedFields = (visibilityRequest) => {
        const list = [];
        visibilityRequest.jobTitle.toLowerCase() === "make_public" && list.push("Profesión");
        visibilityRequest.company.toLowerCase() === "make_public" && list.push("Expresa");
        visibilityRequest.salary.toLowerCase() === "make_public" && list.push("Salario");
        visibilityRequest.technologies.toLowerCase() === "make_public" && list.push("Tecnologías utilizadas");
        visibilityRequest.workPeriod.toLowerCase() === "make_public" && list.push("Periodo laboral");
        return list;
    }

    const cancelNegotiation = () => {
        actionCancel(negotiation.id, getLastAction().offeredData, getLastAction().demandedData, afterOperate);
    }

    const acceptNegotiation = () => {
        actionAccept(negotiation.id, getLastAction().offeredData, getLastAction().demandedData, afterOperate);
    }

    const goToDetails = () => {
        history.push(`/negotiation-details/${negotiation.id}`);
    }

    return (
        <>

            <div className="card">
                <div className="card-body">
                    <div className="row">
                        <div className="col-md-4 d-flex flex-row">
                            <img width="75" height="75" src={negotiation.creator.avatarUrl} className="rounded" alt={negotiation.creator.login} />
                            <div className="p-2">
                                <h6>Creada por {negotiation.creator.login}</h6>
                                <small>{formatDateTimeAgo(negotiation.createdAt)}</small>
                            </div>
                        </div>
                        <div className="col-md-4">
                            {getNegotiationStatus()}
                        </div>
                        <div className="col-md-4 d-flex flex-row-reverse">
                            <img width="75" height="75" src={negotiation.receiver.avatarUrl} className="rounded" alt={negotiation.receiver.login} />
                            <div className="p-2">
                                <h6>Recibida por {negotiation.receiver.login}</h6>
                            </div>
                        </div>
                    </div>
                    {isPending() &&
                        <div className="row mt-2">
                            <div className="col-md-6">
                                {isUser(negotiation.nextActor) &&
                                    <span>El usuario <strong>{getTheOtherUser().login}</strong> te solicita los siguientes datos:</span>
                                }
                                {!isUser(negotiation.nextActor) &&
                                    <span>Has ofrecido a <strong>{getTheOtherUser().login}</strong> los siguientes datos:</span>
                                }
                                {getOfferedFields(getLastAction().offeredData).length <= 0 && <p><span>- Ninguno</span></p>}
                                {getOfferedFields(getLastAction().offeredData).length > 0 &&
                                    <div>
                                        {getOfferedFields(getLastAction().offeredData).map(field => { return <div key={field}>- {field}</div> })}
                                    </div>
                                }
                            </div>
                            <div className="col-md-6">
                                {isUser(negotiation.nextActor) &&
                                    <span>El usuario <strong>{getTheOtherUser().login}</strong> te ofrece los siguientes datos:</span>
                                }
                                {!isUser(negotiation.nextActor) &&
                                    <span>Has solicitado a <strong>{getTheOtherUser().login}</strong> los siguientes datos:</span>
                                }
                                {getOfferedFields(getLastAction().demandedData).length <= 0 && <p><span>- Ninguno</span></p>}
                                {getOfferedFields(getLastAction().demandedData).length > 0 &&
                                    <div>
                                        {getOfferedFields(getLastAction().demandedData).map(field => { return <div key={field}>- {field}</div> })}
                                    </div>
                                }
                            </div>
                        </div>
                    }
                    {isAccepted() &&
                        <div className="row mt-2">
                            <div className="col-md-6">
                                <span>Has compartido los siguientes datos con <strong>{getTheOtherUser().login}</strong>:</span>
                                {getOfferedFields(getLastAction().offeredData).length <= 0 && <p><span>- Ninguno</span></p>}
                                {getOfferedFields(getLastAction().offeredData).length > 0 &&
                                    <div>
                                        {getOfferedFields(getLastAction().offeredData).map(field => { return <div key={field}>- {field}</div> })}
                                    </div>
                                }
                            </div>
                            <div className="col-md-6">
                                <span>El usuario <strong>{getTheOtherUser().login}</strong> ha compartido los siguientes datos:</span>
                                {getOfferedFields(getLastAction().demandedData).length <= 0 && <p><span>- Ninguno</span></p>}
                                {getOfferedFields(getLastAction().demandedData).length > 0 &&
                                    <div>
                                        {getOfferedFields(getLastAction().demandedData).map(field => { return <div key={field}>- {field}</div> })}
                                    </div>
                                }
                            </div>
                        </div>
                    }
                    {!isPending() &&
                        <div className="row">
                            <div className="col-md-12 text-right">
                                <button type="button" className="mr-1 btn btn-outline-primary" onClick={goToDetails}>Ver detalles</button>
                            </div>
                        </div>
                    }
                    {isPending() && isUser(negotiation.nextActor) &&
                        <div className="row">
                            <div className="col-md-12 text-right">
                                <button type="button" className="mr-1 btn btn-outline-danger" onClick={cancelNegotiation}>Cancelar</button>
                                <button type="button" className="mr-1 btn btn-outline-success" onClick={acceptNegotiation}>Aceptar</button>
                                <button type="button" className="mr-1 btn btn-outline-primary" onClick={goToDetails}>Negociar</button>
                            </div>
                        </div>
                    }
                    {isPending() && !isUser(negotiation.nextActor) &&
                        <div className="row">
                            <div className="col-md-12 text-right">
                                <button type="button" className="mr-1 btn btn-outline-danger" onClick={cancelNegotiation}>Cancelar</button>
                                <button type="button" className="mr-1 btn btn-outline-primary" onClick={goToDetails}>Ver detalles</button>
                            </div>
                        </div>
                    }
                </div>
            </div>
        </>
    );
}