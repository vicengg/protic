import React, { useState, useEffect } from 'react';
import { WorkExperienceSummary } from '../WorkExperienceSummary';
import { useParams } from "react-router-dom";
import { useWorkExperience } from '../../hooks/useWorkExperience';
import { useNegotiation } from '../../hooks/useNegotiation';
import { useUser } from '../../hooks/useUser';
import { formatDateTimeAgo } from '../../helpers/dateHelpers';
import { ActionDisplay } from '../ActionDisplay';
import { Modal } from '../Modal';
import { NegotiableWorkExperience } from '../NegotiableWorkExperience';
import { cancel as actionCancel, accept as actionAccept, modify as actionModify } from '../../helpers/negotationActions';

export const NegotiationDetailsView = () => {

    const { data: user } = useUser();

    const { negotiationId } = useParams();
    const [reload, setReload] = useState(false);
    const { data: negotiation, loading: loadingNegotiation } = useNegotiation(negotiationId, reload);

    const [offeredWorkExperienceId, setOfferedWorkExperienceId] = useState(null);
    const [demandedWorkExperienceId, setDemandedWorkExperienceId] = useState(null);

    const { workExperience: offeredWorkExperience, loading: loadingOfferedWorkExperience } = useWorkExperience(offeredWorkExperienceId, reload);
    const { workExperience: demandedWorkExperience, loading: loadingDemandedWorkExperience } = useWorkExperience(demandedWorkExperienceId, reload);

    const [offeredVisibilityRequest, setOfferedVisibilityRequest] = useState(null);
    const [demandedVisibilityRequest, setDemandedVisibilityRequest] = useState(null);

    const [modal, setModal] = useState(false);

    const openModal = () => {
        setModal(true);
    }

    const closeModal = () => {
        setModal(false);
    }


    const getLastAction = () => {
        return !!negotiation && !!negotiation.actions && !!negotiation.actions.length &&
            negotiation.actions[negotiation.actions.length - 1];
    }

    useEffect(() => {
        if (!!negotiation) {
            setOfferedWorkExperienceId(negotiation.offeredWorkExperienceId);
            setDemandedWorkExperienceId(negotiation.demandedWorkExperienceId);
            setOfferedVisibilityRequest(getLastAction().offeredData);
            setDemandedVisibilityRequest(getLastAction().demandedData);
        }
    }, [negotiation]);

    const isNextUser = () => {
        if (!!user && !!negotiation && !!negotiation.nextActor) {
            return negotiation.nextActor.id === user.id;
        } else {
            return false;
        }
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

    const isUser = (otherUser) => {
        return user.id === otherUser.id;
    }

    const getNegotiationStatus = () => {
        if (isAccepted()) {
            return <span className="text-success">Aceptada</span>
        }
        if (isCancelled()) {
            return <span className="text-danger">Cancelada</span>
        }
        if (isUser(negotiation.nextActor)) {
            return <span className="text-primary">Pendiente de tu respuesta</span>
        } else {
            return <span className="text-info">{`Esperando respuesta (de ${negotiation.nextActor.login})`}</span>
        }
    }

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

    const cancelNegotiation = () => {
        actionCancel(negotiation.id, getLastAction().offeredData, getLastAction().demandedData, afterOperate);
    }

    const acceptNegotiation = () => {
        actionAccept(negotiation.id, getLastAction().offeredData, getLastAction().demandedData, afterOperate);
    }

    const modifyNegotiation = () => {
        actionModify(negotiation.id, offeredVisibilityRequest, demandedVisibilityRequest, afterOperate);
    }

    const afterOperate = () => {
        closeModal();
        setReload(!reload);
    }

    return (
        <>
            <div className="container">
                <h1>Detalles de solicitud</h1>
                {!!negotiation && !!user && <div className="row">
                    <div className="col-md-8">
                        <div>
                            <strong>Creada</strong>: {formatDateTimeAgo(new Date(negotiation.createdAt))}
                        </div>
                        <div>
                            <strong>Estado</strong>: {getNegotiationStatus()}
                        </div>
                    </div>
                    <div className="col-md-4 text-right">
                        {isPending() && !isUser(negotiation.nextActor) &&
                            <div>
                                <button type="button" className="mr-1 btn btn-outline-danger" onClick={cancelNegotiation}>Cancelar</button>
                            </div>
                        }
                        {isPending() && isUser(negotiation.nextActor) &&
                            <div>
                                <button type="button" className="mr-1 btn btn-outline-danger" onClick={cancelNegotiation}>Cancelar</button>
                                <button type="button" className="mr-1 btn btn-outline-success" onClick={acceptNegotiation}>Aceptar</button>
                                <button type="button" className="mr-1 btn btn-outline-primary" onClick={openModal}>Negociar</button>
                            </div>
                        }
                    </div>
                </div>
                }
                <div className="row">
                    <div className="col-md-6">
                        {!loadingNegotiation && !loadingOfferedWorkExperience && !!offeredWorkExperience &&
                            <div className="mt-2">
                                <WorkExperienceSummary
                                    workExperience={offeredWorkExperience}
                                    visibilityRequest={offeredVisibilityRequest}
                                    setVisibilityRequest={setOfferedVisibilityRequest}
                                    disabled={!isNextUser()}>
                                </WorkExperienceSummary>
                            </div>
                        }
                    </div>
                    <div className="col-md-6">
                        {!loadingNegotiation && !loadingDemandedWorkExperience && !!demandedWorkExperience &&
                            <div className="mt-2">
                                <WorkExperienceSummary
                                    workExperience={demandedWorkExperience}
                                    visibilityRequest={demandedVisibilityRequest}
                                    setVisibilityRequest={setDemandedVisibilityRequest}
                                    disabled={!isNextUser()}>
                                </WorkExperienceSummary>
                            </div>
                        }
                    </div>
                </div>
                <div className="row mb-5">
                    {!!negotiation && !!user && negotiation.actions.map((action, index) => {
                        return <ActionDisplay
                            key={action.date}
                            action={action}
                            user={user}
                            isFirst={index === 0}
                            creator={negotiation.creator}
                            receiver={negotiation.receiver} />
                    })}
                </div>
                <Modal title="Negociar solicitud" isShown={modal} close={closeModal} additionalClasses="modal-lg">
                    <div className="row">
                        <div className="col-md-6">
                            {!!offeredWorkExperience && <div className="mt-2">
                                <NegotiableWorkExperience workExperience={offeredWorkExperience} visibilityRequest={offeredVisibilityRequest} setVisibilityRequest={setOfferedVisibilityRequest}>
                                    <div className="p1 lead">{workExperienceTitle(offeredWorkExperience)}</div>
                                    {!offeredWorkExperience.binding && <p className="pl-1 pr-1 text-justify text-info">
                                        <small>Esta experiencia no está vinculada a tu perfil y por lo tanto
                                        será visualizada como de origen <strong>anónimo</strong> por el otro usuario.</small>
                                    </p>}
                                </NegotiableWorkExperience>
                            </div>}
                        </div>
                        <div className="col-md-6">
                            {!loadingDemandedWorkExperience && !!demandedWorkExperience && <div className="mt-2">
                                <NegotiableWorkExperience workExperience={demandedWorkExperience} visibilityRequest={demandedVisibilityRequest} setVisibilityRequest={setDemandedVisibilityRequest}>
                                    <div className="p1 lead">{workExperienceTitle(demandedWorkExperience)}</div>
                                    {!demandedWorkExperience.binding && <p className="pl-1 pr-1 text-justify text-warning">
                                        <small>Esta experiencia no está vinculada a ningún perfil
                                        y por lo tanto <strong>no podrás conocer</strong> la identidad del usuario al que pertenece.</small>
                                    </p>}
                                </NegotiableWorkExperience>
                            </div>}
                        </div>
                    </div>
                    <div>
                        <button className="btn btn-outline-secondary mr-1" onClick={closeModal}>Cancelar</button>
                        <button className="btn btn-primary mr-1" onClick={modifyNegotiation}>Enviar</button>
                    </div>
                </Modal>
            </div>
        </>
    );

};