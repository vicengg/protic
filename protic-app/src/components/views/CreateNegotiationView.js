import React, { useState, useEffect } from 'react';
import { Select } from '../Select';
import { NegotiableWorkExperience } from '../NegotiableWorkExperience';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { useParams } from "react-router-dom";
import { useWorkExperience } from '../../hooks/useWorkExperience';
import { RequestButton } from '../RequestButton';
import { create as actionCreate } from '../../helpers/negotationActions';
import { useHistory } from "react-router-dom";

export const CreateNegotiationView = () => {

    const [{ data, loading }] = useGetWorkExperiences({ scope: 'own' });
    const [offeredWorkExperience, setOfferedWorkExperience] = useState(null);
    const { demandedWorkExperienceId } = useParams();
    const [demandedWorkExperience] = useWorkExperience(demandedWorkExperienceId);
    const [offeredVisibilityRequest, setOfferedVisibilityRequest] = useState({});
    const [demandedVisibilityRequest, setDemandedVisibilityRequest] = useState({});

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

    const toPairList = (workExperiences) => {
        if (!!workExperiences) {
            return workExperiences.map((workExperience) => {
                return {
                    key: workExperience.id,
                    value: workExperienceTitle(workExperience)
                }
            })
        } else {
            return [];
        }
    }

    const selectWorkExperience = (id) => {
        setOfferedWorkExperience(data.result.find(workExperience => workExperience.id === id));
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

    useEffect(() => {
        if (!loading) {
            selectWorkExperience(data.result[0].id);
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [loading, data]);

    return (
        <>
            <div className="container">
                <h1>Solicitud de información</h1>
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
                        {!!offeredWorkExperience && <div className="mt-2">
                            <NegotiableWorkExperience workExperience={offeredWorkExperience} visibilityRequest={offeredVisibilityRequest} setVisibilityRequest={setOfferedVisibilityRequest}>
                                <Select values={toPairList(data ? data.result : null)} onChange={selectWorkExperience} />
                                <p className="pl-1 pr-1 pt-1 text-justify">
                                    <small>Ofrece datos sobre una experiencia laboral propia para solicitar
                                        más información sobre la experiencia de otro usuario haciendo uso de los interruptores.</small>
                                </p>
                                {!offeredWorkExperience.binding && <p className="pl-1 pr-1 text-justify text-info">
                                    <small>Esta experiencia no está vinculada a tu perfil y por lo tanto
                                        será visualizada como de origen <strong>anónimo</strong> por el otro usuario.</small>
                                </p>}
                            </NegotiableWorkExperience>
                        </div>}
                    </div>
                    <div className="col-md-6">
                        {!!demandedWorkExperience && <div className="mt-2">
                            <NegotiableWorkExperience workExperience={demandedWorkExperience} visibilityRequest={demandedVisibilityRequest} setVisibilityRequest={setDemandedVisibilityRequest}>
                                <div className="p1 lead">{workExperienceTitle(demandedWorkExperience)}</div>
                                <p className="p-1 text-justify">
                                    <small>Solicita los datos de la experiencia laboral del usuario que deseas conocer haciendo uso de los interruptores.
                                    Puedes hacer más interesante la solicitud para el otro usuario ofreciendo tus propios datos.
                                    </small>
                                </p>
                                {!demandedWorkExperience.binding && <p className="pl-1 pr-1 text-justify text-warning">
                                    <small>Esta experiencia no está vinculada a ningún perfil
                                        y por lo tanto <strong>no podrás conocer</strong> la identidad del usuario al que pertenece.</small>
                                </p>}
                            </NegotiableWorkExperience>
                        </div>}
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