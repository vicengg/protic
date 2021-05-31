import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router';
import { useGetNegotiations } from '../../hooks/useGetNegotiations';
import { useUser } from '../../hooks/useUser';
import { CardSkeleton } from '../CardSkeleton';
import { NegotiationSummary } from '../NegotiationSummary';


export const MyNegotiationsView = () => {

    const skeletonPieces = [...Array(5).keys()];
    const { data: user } = useUser();
    const [{ data: createdNegotiations, loading: creadtedNegotiationsLoading }, reloadCreatorNegotiation] = useGetNegotiations('creator');
    const [{ data: receivedNegotiations, loading: receivedNegotiationsLoading }, reloadReceiverNegotiation] = useGetNegotiations('receiver');
    const [negotiations, setNegotiations] = useState([]);

    const reload = () => {
        reloadCreatorNegotiation();
        reloadReceiverNegotiation();
    }

    const isLoading = () => {
        return creadtedNegotiationsLoading || receivedNegotiationsLoading;
    }


    const history = useHistory();

    const goToSearchExperiences = () => {
        history.push("/search-work-experiences");
    };

    useEffect(() => {

        if (!!createdNegotiations && !!receivedNegotiations) {
            setNegotiations([
                ...createdNegotiations.result.map(element => { return { ...element, createdAt: new Date(element.createdAt) } }),
                ...receivedNegotiations.result.map(element => { return { ...element, createdAt: new Date(element.createdAt) } })
            ].sort((a, b) => b.createdAt - a.createdAt));
        }
    }, [createdNegotiations, receivedNegotiations]);

    return (
        <>
            {user && <div className="container">
                <h1>Mis solicitudes</h1>
                <div className="row">
                    <div className="col-md-12">
                        {isLoading() && skeletonPieces.map(piece =>
                            <div key={piece} className="row mb-2">
                                <div className="col-md-12">
                                    <CardSkeleton lines={8} />
                                </div>
                            </div>
                        )}
                        {!isLoading() && !!negotiations && negotiations.map(negotiation => {
                            return <div key={negotiation.id} className="mb-2">
                                <NegotiationSummary negotiation={negotiation} user={user} afterOperate={reload} />
                            </div>
                        })}
                        {!isLoading() && (!negotiations || negotiations.length === 0) &&
                            <div className="jumbotron">
                                <h1 className="display-4">¡Aún no hay ninguna solicitud!</h1>
                                <p className="lead">Puedes obtener información laboral de otros usuarios solicitandoles datos  sobre su experiencias y ofrenciendoles los tuyos propios.</p>
                                <hr className="my-4" />
                                <p>Para solicitar información de una experiencia puedes ir al panel "Buscar experiencias".</p>
                                <p className="lead">
                                    <button onClick={goToSearchExperiences} className="btn btn-primary btn-lg">Buscar experiencias</button>
                                </p>
                            </div>
                        }
                    </div>
                </div>
            </div>}
        </>
    );

};