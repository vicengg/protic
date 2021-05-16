import { useState, useEffect } from 'react';

export const useGetWorkExperiences = (filters) => {

    const url = '/work-experience?';
    Object.keys(filters).forEach((k) => filters[k] == null && delete filters[k]);
    const queryParams = new URLSearchParams(filters).toString();

    const [state, setState] = useState({ loading: true, error: null, data: null });
    const [reload, setReload] = useState(false);

    const forceReload = () => {
        setReload(true);
    }

    useEffect(() => {
        setReload(false);
        setState({ loading: true, error: null, data: null });
        fetch(url + queryParams)
            .then(response => response.json())
            .then(data => {
                setState({
                    loading: false,
                    error: null,
                    data
                });
            });
    }, [reload, queryParams]);

    return [state, forceReload];
};