interface CachedImage {
    uri: string;
    timestamp: number;
}

class ImageCacheManager {
    private cache = new Map<string, CachedImage>();
    private screenCaches = new Map<string, Set<string>>();

    loadImage(url: string, screenId: string): string {
        // Register the URL with the screen
        if (!this.screenCaches.has(screenId)) {
            this.screenCaches.set(screenId, new Set());
        }
        this.screenCaches.get(screenId)!.add(url);

        // For React Native, we'll return the original URL
        // The Image component handles caching internally
        // We could implement more sophisticated caching with react-native-fast-image
        if (url && url !== 'N/A') {
            this.cache.set(url, {
                uri: url,
                timestamp: Date.now()
            });
            return url;
        }

        return '';
    }

    clearScreenCache(screenId: string): void {
        const urls = this.screenCaches.get(screenId);
        if (urls) {
            urls.forEach(url => this.cache.delete(url));
            this.screenCaches.delete(screenId);
            console.log(`Cleared cache for screen: ${screenId} (${urls.size} images)`);
        }
    }

    clearCache(): void {
        this.cache.clear();
        this.screenCaches.clear();
        console.log('Cleared entire image cache');
    }

    // Clean up old cached entries
    cleanupOldEntries(): void {
        const now = Date.now();
        const maxAge = 300000; // 5 minutes

        for (const [url, cached] of this.cache.entries()) {
            if (now - cached.timestamp > maxAge) {
                this.cache.delete(url);
            }
        }
    }
}

export default new ImageCacheManager();