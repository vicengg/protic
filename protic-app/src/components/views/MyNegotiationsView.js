import React, { useState, useEffect } from 'react';
import { useGetNegotiations } from '../../hooks/useGetNegotiations';
import { useUser } from '../../hooks/useUser';
import { NegotiationSummary } from '../NegotiationSummary';


export const MyNegotiationsView = () => {

    const { data: user } = useUser();
    const [{ data: createdNegotiations }, reloadCreatorNegotiation] = useGetNegotiations('creator');
    const [{ data: receivedNegotiations }, reloadReceiverNegotiation] = useGetNegotiations('receiver');
    const [negotiations, setNegotiations] = useState([]);

    const reload = () => {
        reloadCreatorNegotiation();
        reloadReceiverNegotiation();
    }

    useEffect(() => {

        if (!!createdNegotiations && !!receivedNegotiations) {
            setNegotiations([
                ...createdNegotiations.result.map(element => { return { ...element, createdAt: new Date(element.createdAt) } }),
                ...receivedNegotiations.result.map(element => { return { ...element, createdAt: new Date(element.createdAt) } })
            ])
        }
    }, [createdNegotiations, receivedNegotiations]);

    return (
        <>
            {user && <div className="container">
                <h1>Mis solicitudes</h1>
                <div className="row">
                    <div className="col-md-12">
                        {!!negotiations && negotiations.map(negotiation => {
                            return <div  key={negotiation.id} className="mb-2">
                                <NegotiationSummary negotiation={negotiation} user={user} afterOperate={reload}/>
                            </div>
                        })}
                    </div>
                </div>
            </div>}
        </>
    );

};