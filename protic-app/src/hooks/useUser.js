import { useState, useEffect } from 'react';

export const useUser = () => {

    const url = '/user';

    const [state, setState] = useState({ loading: true, error: null, data: null });

    useEffect(() => {
        setState({ loading: true, error: null, data: null });
        fetch(url)
            .then(response => response.json())
            .then(data => {
                setState({
                    loading: false,
                    error: null,
                    data
                });
            });
    }, []);

    return state;
};