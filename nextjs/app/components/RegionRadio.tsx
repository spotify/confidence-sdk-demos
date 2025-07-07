'use client'

interface RegionRadioProps {
    onRegionChange?: (region: string) => void
    defaultRegion?: string
    value?: string
    className?: string
}

const regions = [
    {value: 'na', label: 'North America'},
    {value: 'eu', label: 'Europe'},
    {value: 'ap', label: 'Asia Pacific'},
    {value: 'sa', label: 'South America'},
    {value: 'af', label: 'Africa'}
]

export default function RegionRadio({
    onRegionChange,
    value,
    className = ''
}: RegionRadioProps) {

    const handleRegionChange = (region: string) => {
        onRegionChange?.(region)
    }

    return (
        <div className={`space-y-3 ${className}`}>
            <div className="space-y-2">
                {regions.map((region) => (
                        <label
                            key={region.value}
                            className="flex items-center space-x-2 cursor-pointer hover:text-neutral-700"
                        >
                            <input
                                type="radio"
                                name="region"
                                value={region.value}
                                checked={value === region.value}
                                onChange={(e) => handleRegionChange(e.target.value)}
                                className="w-4 h-4 accent-neutral-900 border-gray-300 focus:ring-neutral-500"
                            />
                            <span className="text-sm text-gray-700">
                                {region.label}
                            </span>
                        </label>
                ))}
            </div>
        </div>
    )
}
