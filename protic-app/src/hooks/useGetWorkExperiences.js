import { useState, useEffect } from 'react';

export const useGetWorkExperiences = (scope, jobTitle) => {

    const url = '/work-experience?';
    const params = { scope, jobTitle }
    Object.keys(params).forEach((k) => params[k] == null && delete params[k]);
    const queryParams = new URLSearchParams(params).toString();

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