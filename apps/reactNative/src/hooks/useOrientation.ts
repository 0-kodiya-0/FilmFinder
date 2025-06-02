import { useState, useEffect } from 'react';
import { Dimensions } from 'react-native';

interface ScreenData {
    width: number;
    height: number;
    isLandscape: boolean;
    isPortrait: boolean;
}

export const useOrientation = (): ScreenData => {
    const [screenData, setScreenData] = useState(() => {
        const { width, height } = Dimensions.get('window');
        return {
            width,
            height,
            isLandscape: width > height,
            isPortrait: height > width,
        };
    });

    useEffect(() => {
        const subscription = Dimensions.addEventListener('change', ({ window }) => {
            console.log('Orientation changed:', {
                width: window.width,
                height: window.height,
                isLandscape: window.width > window.height
            });

            setScreenData({
                width: window.width,
                height: window.height,
                isLandscape: window.width > window.height,
                isPortrait: window.height > window.width,
            });
        });

        return () => subscription?.remove();
    }, []);

    return screenData;
};