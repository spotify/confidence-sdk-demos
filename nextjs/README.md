# Confidence Web Demo

This project is an example application that showcases the implementation of [Confidence js SDK](https://github.com/spotify/confidence-sdk-js) using [OpenFeature](https://openfeature.dev/) in a Next.js project using the App Router (bootstrapped with [`create-next-app`](https://nextjs.org/docs/app/api-reference/cli/create-next-app)). It demonstrates how to integrate feature flags into a web application with server-side rendering using a fictional software subscription website. 

The application evaluates feature flags using a context that includes `visitor_id` and `region`. The `visitor_id` is extracted from cookies using Next.js middleware, while the `region` is extracted from the URL search parameters. You can modify these values using the button in the lower right corner of the app to see how different users receive different experiences based on their context.


## Running the Project Locally

### Prerequisites

- Node.js (version 18 or higher)
- npm, yarn or pnpm package manager

### Setup

1. Clone the repository and navigate to the project directory:
```bash
cd confidence-sdk-demos/nextjs
```

2. Install dependencies:
```bash
npm install
# or
yarn install
# or
pnpm install
```

3. Set up environment variables:
Create a `.env.local` file in the root directory and add the following Confidence client secret:
```
NEXT_PUBLIC_CLIENT_SECRET=BEFwpwAWFupTtxEyt7ukdIc5hwAC7Lxc
```
This will connect the project to an existing Confidence demo account with pre-configured feature flags that are used in the application.

4. Run the development server:
```bash
npm run dev
# or
yarn dev
# or
pnpm dev
```

5. Open [http://localhost:3000](http://localhost:3000) in your browser to see the application.


## Feature Flags Configuration

This project connects to a Confidence demo account containing two feature flags with specific targeting rules:

### `subscription-highlight`
- **Schema**: 
```json
{
    "enabled": boolean
}
```

- **Purpose**: Controls highlighting of a Premium subscription plan with "Most Popular" badge
- **Targeting Rule**: 50/50 random allocation based on visitor ID
  - 50% receive treatment (highlighted Premium plan)
  - 50% receive control (standard Premium plan)

### `show-enterprise-plan`
- **Schema**: 
```json
{
    "enabled": boolean,
    "price": string
}
```
- **Purpose**: Controls Enterprise plan visibility and pricing
- **Targeting Rule**: Region-based targeting
  - **North America (`na`)**: Shows Enterprise plan with the price "$999"
  - **Europe (`eu`)**: Shows Enterprise plan with "Let's Talk" price
  - **Other regions**: Enterprise plan hidden


### Server-Side Feature Flag Fetching

The application uses **server-side rendering** to fetch feature flags before rendering the page, ensuring that users see the correct content immediately without client-side flashing. Feature flags are evaluated using the `visitor_id` and `region` as context parameters.

## Architecture Details

- **Framework**: Next.js 14 with App Router
- **Feature Flags**: Confidence SDK with OpenFeature
- **Styling**: Tailwind CSS
- **TypeScript**: Full type safety
- **Rendering**: Server-side with hydration

## Documentation References

- **[Confidence JavaScript SDK Documentation](https://github.com/spotify/confidence-sdk-js)**
- **[OpenFeature Node.js Documentation](https://openfeature.dev/docs/reference/technologies/server/javascript/)**
- **[Next.js Documentation](https://nextjs.org/docs)**

