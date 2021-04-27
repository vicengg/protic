import React, { useState, useEffect, useRef } from 'react';
import { useFetch } from '../hooks/useFetch'
import { useResize } from '../hooks/useResize'

export const Autocomplete = ({ url, placeholder, label, footer, value, onChange, onSelect, onSubmit }) => {

    const [suggestionsMenuVisibility, setSuggestionsMenuVisibility] = useState(false);
    const {loading, data} = useFetch(`${url}${value}`);
    const [suggestions, setSuggestions] = useState([]);
    const [keyboardSelection, setKeyboardSelection] = useState(null);
    const inputRef = useRef();
    const suggestionsSize = useResize(inputRef);

    const clearKeyboardSelection = () => {
        setKeyboardSelection(null);
    }

    const openSuggestionsMenu = () => {
        setSuggestionsMenuVisibility(true);
    }

    const closeSuggestionsMenu = () => {
        setSuggestionsMenuVisibility(false);
    }

    const handleInputChange = (event) => {
        clearKeyboardSelection();
        openSuggestionsMenu();
        onChange(event.target.value);
    };

    const handleInputFocus = () => {
        openSuggestionsMenu();
    };

    const handleInputBlur = () => {
        closeSuggestionsMenu();
    };

    const handleSuggestionClick = (event) => {
        onSelect(event.target.innerText);
        closeSuggestionsMenu();
    };

    const handleFormSubmit = (event) => {
        event.preventDefault();
        onSubmit(keyboardSelection);
        closeSuggestionsMenu();
    };

    const handleInputKeyDown = (event) => {
        console.log(inputRef);
        switch(event.key) {
            case "ArrowUp":
                changeKeyboardSelecction(-1);
                break;
            case "ArrowDown":
                changeKeyboardSelecction(1);
                break;
        }
    };

    const changeKeyboardSelecction = (increasement) => {
        if (!!suggestions && suggestions.length > 0) {
            if(!!keyboardSelection) {
                const index = suggestions.indexOf(keyboardSelection);
                setKeyboardSelection(suggestions[index + increasement >= 0 ? index + increasement : suggestions.length - increasement]);
            } else {
                if(increasement === 1) {
                    setKeyboardSelection(suggestions[0]);
                } else if(increasement === -1) {
                    setKeyboardSelection(suggestions[suggestions.length - 1]);
                }
            }
        }
    }

    useEffect(() => {
        if(!loading) {
            setSuggestions(data.data);
        }
    }, [loading]);

    useEffect(() => {
        if(!loading) {
            setSuggestions(data.data);
        }
    }, [loading]);


    return (
        <>
            <form onSubmit={ handleFormSubmit } autoComplete="off" className="component-autocomplete">
                <div className="input-container form-group">
                        <label>{ label }</label>
                        <input 
                            ref={ inputRef }
                            className="form-control" 
                            type="text" 
                            value={ value } 
                            onChange={ handleInputChange } 
                            placeholder={ placeholder }
                            onFocus={ handleInputFocus }
                            onKeyDown={ handleInputKeyDown }
                            onBlur={ handleInputBlur }
                        />
                        <div className="suggestions" style={{width: suggestionsSize.width}}>
                        {!!suggestionsMenuVisibility && !!suggestions && suggestions.map(element => {
                                return <div 
                                onMouseDown={ handleSuggestionClick } 
                                className={ element === keyboardSelection ? "suggestion p-1 bg-primary" : "suggestion p-1"}
                                key={element}>{element}</div>
                            })
                        }</div>
                        {!!footer && <small className="form-text text-muted">{ footer }</small>}
                </div>
            </form>
        </>
    );

};
