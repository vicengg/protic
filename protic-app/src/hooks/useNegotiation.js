import { useState, useEffect } from 'react';

export const useNegotiation = (negotiationId) => {

    const [negotiation, setNegotiation] = useState({ data: null, loading: true });


    useEffect(() => {
        fetch(`/negotiation/${negotiationId}`)
            .then(response => response.json())
            .then(data => {
                setNegotiation({ data, loading: false });
            });
    }, [negotiationId]);



    return negotiation;
};