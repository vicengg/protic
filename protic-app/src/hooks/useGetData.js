import {useState, useEffect} from 'react';

export const useGetData = (url) =>  {

    const [state, setState] = useState({loading: true, error: null, data: null});

    useEffect(() => {
        setState({loading: true, error: null, data: null});
        fetch(url)
        .then(response => response.json())
        .then(data => {
            setState({
                loading: false,
                error: null,
                data
            });
        });
    }, [url]);

    return state;
};