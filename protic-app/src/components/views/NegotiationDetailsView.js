import React, { useState, useEffect } from 'react';
import { NegotiableWorkExperience } from '../NegotiableWorkExperience';
import { useParams } from "react-router-dom";
import { useWorkExperience } from '../../hooks/useWorkExperience';
import { RequestButton } from '../RequestButton';
import { create as actionCreate } from '../../helpers/negotationActions';
import { useHistory } from "react-router-dom";
import { useNegotiation } from '../../hooks/useNegotiation';
import { useUser } from '../../hooks/useUser';

export const NegotiationDetailsView = () => {

    const { data: user } = useUser();

    const { negotiationId } = useParams();
    const { data: negotiation , loading: loadingNegotiation } = useNegotiation(negotiationId);

    const [offeredWorkExperienceId, setOfferedWorkExperienceId] = useState(null);
    const [demandedWorkExperienceId, setDemandedWorkExperienceId] = useState(null);

    const { workExperience: offeredWorkExperience, loading: loadingOfferedWorkExperience } = useWorkExperience(offeredWorkExperienceId);
    const { workExperience: demandedWorkExperience, loading: loadingDemandedWorkExperience } = useWorkExperience(demandedWorkExperienceId);

    const [offeredVisibilityRequest, setOfferedVisibilityRequest] = useState(null);
    const [demandedVisibilityRequest, setDemandedVisibilityRequest] = useState(null);

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



    const history = useHistory();

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


    const addAction = (response) => {
        if (response.status === 200) {
            response.json().then(negotiation => {
                actionCreate(negotiation.id, offeredVisibilityRequest, demandedVisibilityRequest, response => {
                    history.replace("/my-negotiations");
                });
            });
        }
    }

    const isNextUser = () => {
        if(!!user && !!negotiation && !!negotiation.nextActor) {
            return negotiation.nextActor.id === user.id;
        } else {
            return false;
        }
    }


    return (
        <>
            <div className="container">
                <h1>Detalles de solicitud</h1>
                <div className="row">
                    <div className="col-md-6">
                        <h2>Información ofrecida</h2>
                    </div>
                    <div className="col-md-6">
                        <h2>Información solicitada</h2>
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-6">
                        {!loadingNegotiation && !loadingOfferedWorkExperience && !!offeredWorkExperience &&
                            <div className="mt-2">
                                <NegotiableWorkExperience
                                    workExperience={offeredWorkExperience}
                                    visibilityRequest={offeredVisibilityRequest}
                                    setVisibilityRequest={setOfferedVisibilityRequest}
                                    disabled={!isNextUser()}>
                                    <div className="p1 lead">{workExperienceTitle(offeredWorkExperience)}</div>
                                    <p className="p-1 text-justify">
                                        <small>Solicita los datos de la experiencia laboral del usuario que deseas conocer haciendo uso de los interruptores.
                                        Puedes hacer más interesante la solicitud para el otro usuario ofreciendo tus propios datos.
                                    </small>
                                    </p>
                                </NegotiableWorkExperience>
                            </div>
                        }
                    </div>
                    <div className="col-md-6">
                        {!loadingNegotiation && !loadingDemandedWorkExperience && !!demandedWorkExperience &&
                            <div className="mt-2">
                                <NegotiableWorkExperience
                                    workExperience={demandedWorkExperience}
                                    visibilityRequest={demandedVisibilityRequest}
                                    setVisibilityRequest={setDemandedVisibilityRequest}
                                    disabled={!isNextUser()}>
                                    <div className="p1 lead">{workExperienceTitle(demandedWorkExperience)}</div>
                                    <p className="p-1 text-justify">
                                        <small>Solicita los datos de la experiencia laboral del usuario que deseas conocer haciendo uso de los interruptores.
                                        Puedes hacer más interesante la solicitud para el otro usuario ofreciendo tus propios datos.
                                    </small>
                                    </p>
                                </NegotiableWorkExperience>
                            </div>
                        }
                    </div>
                </div>
                <div className="row mt-3">
                    <div className="col-md-12 text-right">
                        <RequestButton
                            text="Solicitar información"
                            url="/negotiation"
                            method="POST"
                            body={!!offeredWorkExperience && !!demandedWorkExperience && { offeredWorkExperienceId: offeredWorkExperience.id, demandedWorkExperienceId: demandedWorkExperience.id }}
                            onSuccess={addAction} />
                    </div>
                </div>
            </div>
        </>
    );

};