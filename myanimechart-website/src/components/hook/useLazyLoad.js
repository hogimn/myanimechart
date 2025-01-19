import {useState, useEffect} from 'react';

const useLazyLoad = () => {
    const [ref, setRef] = useState(null);
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const observer = new IntersectionObserver(([entry]) => {
            if (entry.isIntersecting) {
                setIsVisible(true);
                observer.disconnect();
            }
        });

        if (ref) {
            observer.observe(ref);
        }

        return () => {
            if (ref) observer.disconnect();
        };
    }, [ref]);

    return [setRef, isVisible];
};

export default useLazyLoad;
