import {useState, useEffect} from 'react';

export const useStateDelay = (initial, delay) =>  {

    const [state, setState] = useState(initial);
    const [timeoutHandler, setTimeoutHandler] = useState(null);
    const [stateDelay, setStateDelay] = useState(state);

    useEffect(() => {
        clearTimeout(timeoutHandler);
        setTimeoutHandler(setTimeout(() => setStateDelay(state), delay));
    }, [state]);

    return [state, stateDelay, setState]

};